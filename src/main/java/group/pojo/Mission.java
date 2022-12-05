package group.pojo;

import group.pojo.util.MyTime;
import org.omg.CORBA.INTERNAL;

import java.util.*;

public class Mission {
    private static int count = 1;

    String missionID;
    MyTime time;
    String place;
    String title;
    String description;
    int status;
    Map<String, Integer> reporterNeeds;
    Map<String, List<User>> reporters;

    public Mission() {
        count++;
    }

    public Mission(MyTime time, String place, String title,
                   String description, Map<String, Integer> reporterNeeds) {
        this.time = time;
        this.place = place;
        this.title = title;
        this.description = description;
        this.status = 0;
        this.reporterNeeds = reporterNeeds;
        this.reporters = new HashMap<>();
        for (String str : reporterNeeds.keySet()
        ) {
            reporters.put(str, new ArrayList<>());
        }
        // 设置任务号
        if (count < 10) {
            this.missionID = time.getDataCode() + "0" + count;
        } else {
            this.missionID = time.getDataCode() + count;
        }
        // count累加
        count++;
        // 控制在两位数内
        if (count == 99) {
            count = 1;
        }
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Mission.count = count;
    }

    public String getMissionID() {
        return missionID;
    }

    public void setMissionID(String missionID) {
        this.missionID = missionID;
    }

    public MyTime getTime() {
        return time;
    }

    public void setTime(MyTime time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, Integer> getReporterNeeds() {
        return reporterNeeds;
    }

    public void setReporterNeeds(Map<String, Integer> reporterNeeds) {
        this.reporterNeeds = reporterNeeds;
    }

    public Map<String, List<User>> getReporters() {
        return reporters;
    }

    public void setReporters(Map<String, List<User>> reporters) {
        this.reporters = reporters;
    }
}
