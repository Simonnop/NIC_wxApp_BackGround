package group.pojo.util;

import java.util.Map;

public class MyTime {
    int year;
    int month;
    int day;
    int hour;
    int minute;

    public MyTime(Map<String,Integer> time) {
        this.year = time.get("year");
        this.month = time.get("month");
        this.day = time.get("day");
        this.hour = time.get("hour");
        this.minute = time.get("minute");
    }

    public MyTime(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public MyTime() {
    }

    public String getDate() {
        return "" + year + "/" + month + "/" + day + "";
    }

    public String getDataCode() {
        String gap1 = "";
        String gap2 = "";
        if (month < 10) {
            gap1 = "0";
        }
        if (day < 10) {
            gap2 = "0";
        }
        return "" + year + gap1 + month + gap2 + day;
    }

    public String getTime() {
        return "" + year + "/" + month + "/" + day + "-" + hour + ":" + minute + "";
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
