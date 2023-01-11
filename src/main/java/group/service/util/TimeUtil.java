package group.service.util;

import com.alibaba.fastjson.JSONObject;
import group.dao.impl.ConfigDaoImpl;
import org.bson.Document;

import java.util.*;

public class TimeUtil {
    public static int[] changeTimeToInts(String time) {

        String[] strings = time.split(":");
        int[] HourWithMinute = new int[2];
        for (int i = 0; i < strings.length; i++) {
            HourWithMinute[i] = Integer.parseInt(strings[i]);
        }
        return HourWithMinute;
    }

    public static Integer[] getWeekDayByTime(Map<String, Integer> time) {
        // 根据年月日获取周数与星期
        ConfigDaoImpl configDaoImpl = ConfigDaoImpl.getConfigDaoImpl();

        Document firstDay = (Document) configDaoImpl.showItemByInput("item", "timetable").first().get("firstDay");

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.set(
                (Integer) firstDay.get("year"),
                (Integer) firstDay.get("month") - 1,
                (Integer) firstDay.get("day"));
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(
                time.get("year"),
                time.get("month") - 1,
                time.get("day"));

        int week = 1;
        int count = 0;
        while (beginCalendar.get(Calendar.YEAR) != currentCalendar.get(Calendar.YEAR)
               || beginCalendar.get(Calendar.MONTH) != currentCalendar.get(Calendar.MONTH)
               || beginCalendar.get(Calendar.DATE) != currentCalendar.get(Calendar.DATE)
        ) {
            count++;
            beginCalendar.add(Calendar.DATE, 1);
            if (count == 7) {
                week++;
                count = 0;
            }
        }

        int weekDay = currentCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekDay == 0) {
            weekDay = 7;
        }

        return new Integer[]{week, weekDay};
    }

    public static boolean checkAvailable(int[] missionStartTime, int[] missionEndTime,
                                         int[] classStartTime, int[] classEndTime) {
        // 判断课程时间与任务时间的关系
        // 以下为时间线示意图
        // 任务: ......000000000000000000000000000.......
        // 课程: ...00000000.............................
        if (classEndTime[0] > missionStartTime[0] && classStartTime[0] < missionStartTime[0]) {
            return false;
        } else if (classEndTime[0] == missionStartTime[0] && classEndTime[1] > missionStartTime[1]) {
            return false;
        }
        // 任务: ......000000000000000000000000000.......
        // 课程: .............................00000000...
        if (missionEndTime[0] > classStartTime[0] && missionEndTime[0] < classEndTime[0]) {
            return false;
        } else if (missionEndTime[0] == classStartTime[0] && missionEndTime[1] > classStartTime[1]) {
            return false;
        }
        // 任务: ......000000000000000000000000000.......
        // 课程: ...............00000000.................
        if (missionEndTime[0] > classEndTime[0] && missionStartTime[0] < classStartTime[0]) {
            return false;
        }
        // 全部无冲突
        return true;
    }

    public static String getSeason(Integer week) {
        // 根据周数获取夏冬令时
        ConfigDaoImpl configDao = ConfigDaoImpl.getConfigDaoImpl();
        String season;

        Document document = configDao.showItemByInput("item", "timetable").first();

        int winterWeek = (int) document.get("winterBegin");
        int summerWeek = (int) document.get("summerBegin");

        if (winterWeek > summerWeek) {
            if (week < winterWeek) {
                return "summer";
            } else {
                return "winter";
            }
        } else {
            if (week < summerWeek) {
                return "winter";
            } else {
                return "summer";
            }
        }
    }

    public static JSONObject getCurrentWeekInfo() {
        // 获取当前是第几周星期几
        Integer[] weekDayByTime = TimeUtil.getWeekDayByTime(new HashMap<String, Integer>() {{
            Calendar calendar = Calendar.getInstance();
            put("year", calendar.get(Calendar.YEAR));
            put("month", calendar.get(Calendar.MONTH)+1);
            put("day", calendar.get(Calendar.DATE));
        }});
        return new JSONObject() {{
            put("week", weekDayByTime[0]);
            put("weekDay", weekDayByTime[1]);
        }};
    }

}
