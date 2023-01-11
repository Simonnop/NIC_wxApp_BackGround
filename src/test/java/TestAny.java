import group.dao.MissionDao;
import group.dao.impl.ConfigDaoImpl;
import group.dao.impl.LessonDaoImpl;
import group.dao.impl.MissionDaoImpl;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.service.util.TimeUtil;
import org.bson.Document;
import org.junit.Test;

import java.util.*;

public class TestAny {
    @Test
    public void testFindAvailable() {
        final MissionDao missionDao = MissionDaoImpl.getMissionDao();
        ConfigDaoImpl configDaoImpl = ConfigDaoImpl.getConfigDaoImpl();
        Document timetable = configDaoImpl.showItemByInput("item", "timetable").first();
        Document mission = missionDao.searchMissionByInput("missionID", "2022123050").first();
        if (mission == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }

        // 拿任务的时间
        Map<String, Integer> time = (Map<String, Integer>) mission.get("time");
        // 查询第几周星期几
        Integer[] weekDayByTime = TimeUtil.getWeekDayByTime(time);
        System.out.println(Arrays.toString(weekDayByTime));
        // 查询夏令时或冬令时
        String season = TimeUtil.getSeason(weekDayByTime[0]);

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

            if (lessonOfDay == null) {
                System.out.println(document.get("userid"));
                continue;
            }

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

            System.out.println(document.get("userid"));
        }




    }

    @Test
    public void testDay() {

        System.out.println(TimeUtil.getCurrentWeekInfo());



    }
}
