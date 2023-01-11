package group.service.impl;

import com.mongodb.client.FindIterable;
import group.dao.LessonDao;
import group.dao.MissionDao;
import group.dao.impl.ConfigDaoImpl;
import group.dao.impl.LessonDaoImpl;
import group.dao.impl.MissionDaoImpl;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.pojo.Mission;
import group.service.ManagerService;
import group.service.helper.MissionHelper;
import group.service.util.TimeUtil;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManagerServiceImpl implements ManagerService {

    final MissionDao missionDao = MissionDaoImpl.getMissionDao();
    final LessonDao lessonDao = LessonDaoImpl.getLessonDao();
    final ConfigDaoImpl configDao = ConfigDaoImpl.getConfigDaoImpl();
    final MissionHelper missionManager = MissionHelper.getMissionHelper();

    @Override
    public void addMission(Mission mission) {
        // 初始化任务id与状态
        mission.initializeMission();
        // 添加任务
        missionDao.addMission(mission);
    }

    @Override
    public ArrayList<Document> showMissionGotDraft() {

        FindIterable<Document> documents = missionDao.showAll();
        if (documents.first() == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        ArrayList<Document> documentArrayList = missionManager.changeFormAndCalculate(documents);

        // 判断是否缺人
        documentArrayList.removeIf(document -> ((Document) document
                .get("status"))
                .get("写稿")
                .equals("未达成"));
        return documentArrayList;
    }

    @Override
    public void recommendMission(String missionID, String method) {
        /*
        * TODO
        *  根据 tags 和 用户画像 的 拟合程度 推送
        *  根据 课表情况 推送
        *  采用 socket 与 python 通信
        *
        * */

    }

    public ArrayList<String> findAvailableReporters(String missionID, Integer... intervals) {

        ArrayList<String> reportersList = new ArrayList<>();

        // 拿任务的时间
        Document mission = missionDao.searchMissionByInput("missionID", missionID).first();
        if (mission == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        Map<String, Integer> time = (Map<String, Integer>) mission.get("time");

        // 查询第几周星期几
        Integer[] weekDayByTime = TimeUtil.getWeekDayByTime(time);
        // 查询夏令时或冬令时
        String season = TimeUtil.getSeason(weekDayByTime[0]);
        // 拿时间表
        Document timetable = configDao.showItemByInput("item", "timetable").first();

        loop:
        // 遍历每个人当天的课表
        for (Document document : LessonDaoImpl.getLessonDao().showAll()) {
            // 拿当天的课程
            List<Document> lessonOfDay = document.getList("lessons", Document.class)
                    .get(weekDayByTime[0] - 1)
                    .getList("time", Document.class)
                    .get(weekDayByTime[1] - 1)
                    .getList("lesson", Document.class);
            System.out.println(lessonOfDay);
            // 没课,加入
            if (lessonOfDay == null) {
                reportersList.add((String) document.get("userid"));
                continue;
            }
            // 遍历所有的课
            for (Document lessons : lessonOfDay) {
                String[] times = ((String) lessons.get("time")).split("-");
                for (int i = Integer.parseInt(times[0]); i <= Integer.parseInt(times[1]); i++) {

                    System.out.println(lessons);
                    Document singleLesson = timetable.getList(season, Document.class).get(i - 1);

                    int[] classStartTime = new int[2];
                    String[] startTimes = singleLesson.get("startTime", String.class).split(":");
                    for (int j = 0; j < startTimes.length; j++) {
                        classStartTime[j] = Integer.parseInt(startTimes[j]);
                    }

                    int[] classEndTime = new int[2];
                    String[] endTimes = singleLesson.get("endTime", String.class).split(":");
                    for (int j = 0; j < endTimes.length; j++) {
                        classEndTime[j] = Integer.parseInt(endTimes[j]);
                    }

                    boolean checkAvailable = TimeUtil.checkAvailable(
                            new int[]{time.get("beginHour"), time.get("beginMinute")},
                            new int[]{time.get("endHour"), time.get("endMinute")},
                            classStartTime, classEndTime
                    );

                    if (!checkAvailable) {
                        System.out.println("next");
                        continue loop;
                    }
                }
            }
            // 遍历所有课程都没问题,加入
            reportersList.add((String) document.get("userid"));
        }

        return reportersList;
    }
}
