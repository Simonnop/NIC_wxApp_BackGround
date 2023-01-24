package group.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import group.dao.LessonDao;
import group.dao.MissionDao;
import group.dao.impl.ConfigDaoImpl;
import group.dao.impl.LessonDaoImpl;
import group.dao.impl.MissionDaoImpl;
import group.dao.impl.WorkDaoImpl;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.pojo.WorkFlow;
import group.pojo.part.*;
import group.pojo.util.DocUtil;
import group.service.ManagerService;
import group.service.helper.MissionHelper;
import group.service.util.TimeUtil;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

public class ManagerServiceImpl implements ManagerService {

    final MissionDao missionDao = MissionDaoImpl.getMissionDao();
    final LessonDao lessonDao = LessonDaoImpl.getLessonDao();
    final ConfigDaoImpl configDao = ConfigDaoImpl.getConfigDaoImpl();
    final MissionHelper missionManager = MissionHelper.getMissionHelper();
    final WorkDaoImpl workDao = WorkDaoImpl.getWorkDaoImpl();
    final MissionHelper missionHelper = MissionHelper.getMissionHelper();

    @Override
    public void addMission(JSONObject jsonObject) {

        // 添加任务
        WorkFlow workFlow = new WorkFlow();

        WorkPart workPart = new MissionPart().initPart(workFlow, jsonObject);
        workFlow.insertWorkPart(workPart);

        EnlistPart enlistPart = new EnlistPart();
        enlistPart.setAccordingPartIndex(0);
        enlistPart.setDescription("来人");
        workFlow.insertWorkPart(enlistPart.initPart(workFlow,null));

        SubmitPart submitPart = new SubmitPart();
        submitPart.setAccordingPartIndex(1);
        workFlow.insertWorkPart(submitPart.initPart(workFlow, null));

        ExaminePart examinePart = new ExaminePart();
        examinePart.setAccordingPartIndex(2);
        workFlow.insertWorkPart(examinePart.initPart(workFlow, null));

        workFlow.checkProgress();
        workDao.addWorkFlow(workFlow);
    }

    @Override
    public ArrayList<Document> showMissionGotDraft() {

        FindIterable<Document> documents = workDao.showAll();
        ArrayList<Document> documentArrayList = new ArrayList<>();
        for (Document document : documents) {
            WorkFlow workFlow = DocUtil.doc2Obj(document, WorkFlow.class);
            Integer progressIndex = workFlow.getProgressIndex();
            if (progressIndex == null) {
                continue;
            }
            if (workFlow.getPartsKind().get(progressIndex).equals("ExaminePart")) {
                documentArrayList.add(missionHelper.calculateLack(workFlow.getParts().get(progressIndex)));
            }
        }
        System.out.println(documentArrayList);
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

        /*
         * TODO
         *  适配现有数据结构
         *
         * */
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
                // 获取课程是第几节
                String[] times = ((String) lessons.get("time")).split("-");
                for (int i = Integer.parseInt(times[0]); i <= Integer.parseInt(times[1]); i++) {
                    // 从时间表拿对应课的时间
                    Document singleLesson = timetable.getList(season, Document.class).get(i - 1);
                    // 获取开始时间(时+分)
                    int[] classStartTime = TimeUtil.changeTimeToInts(
                            singleLesson.get("startTime", String.class));
                    // 获取结束时间(时+分)
                    int[] classEndTime = TimeUtil.changeTimeToInts(
                            singleLesson.get("endTime", String.class));
                    // 判断与任务时间是否冲突
                    boolean checkAvailable = TimeUtil.checkAvailable(
                            new int[]{time.get("beginHour"), time.get("beginMinute")},
                            new int[]{time.get("endHour"), time.get("endMinute")},
                            classStartTime, classEndTime
                    );
                    // 冲突,则换下一个人
                    if (!checkAvailable) {
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
