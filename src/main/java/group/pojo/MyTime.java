package group.pojo;

import java.util.Map;

public class MyTime {
    int year;
    int month;
    int day;
    int beginHour;
    int beginMinute;
    int endHour;
    int endMinute;

    public MyTime(Map<String,Integer> time) {
        this.year = time.get("year");
        this.month = time.get("month");
        this.day = time.get("day");
        this.beginHour = time.get("beginHour");
        this.beginMinute = time.get("beginMinute");
        this.endHour = time.get("endHour");
        this.endMinute = time.get("endMinute");
    }

    public MyTime(int year, int month, int day, int beginHour,
                  int beginMinute, int endHour, int endMinute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.beginHour = beginHour;
        this.beginMinute = beginMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
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
        return "" + year + "/" + month + "/" + day + "-" + beginHour + ":" + beginMinute + "";
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

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public int getBeginMinute() {
        return beginMinute;
    }

    public void setBeginMinute(int beginMinute) {
        this.beginMinute = beginMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    @Override
    public String toString() {
        return "MyTime{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", beginHour=" + beginHour +
                ", beginMinute=" + beginMinute +
                ", endHour=" + endHour +
                ", endMinute=" + endMinute +
                '}';
    }
}
