package group.service.util;

import group.dao.ConfigDao;
import group.dao.LessonDao;
import group.dao.impl.ConfigDaoImpl;
import group.dao.impl.LessonDaoImpl;
import org.bson.Document;

import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeUtil {
    public static Integer[] changeTimeToInteger(String time) {

        String[] strings = time.split(":");
        Integer[] HourWithMinute = new Integer[2];
        for (int i = 0; i < strings.length; i++) {
            HourWithMinute[i] = Integer.parseInt(strings[i]);
        }
        return HourWithMinute;
    }

    public static Integer[] getWeekDayByTime(Map<String, Integer> time) {

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

        if (classEndTime[0] > missionStartTime[0] && classStartTime[0] < missionStartTime[0]) {
            return false;
        } else if (classEndTime[0] == missionStartTime[0] && classEndTime[1] > missionStartTime[1]) {
            return false;
        }

        if (missionEndTime[0] > classStartTime[0] && missionEndTime[0] < classEndTime[0]) {
            return false;
        } else if (missionEndTime[0] == classStartTime[0] && missionEndTime[1] > classStartTime[1]) {
            return false;
        }

        if (missionEndTime[0] > classEndTime[0] && missionStartTime[0] < classStartTime[0]) {
            return false;
        }

        return true;
    }

    public static String getSeason(Integer week) {

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


}
