package group.pojo;

import java.text.SimpleDateFormat;
import java.util.*;

public class Mission {
    private static int count = 1;

    String missionID;
    int element;  // 任务属性
    MyTime time;
    String place;
    String title;
    String publisher;
    String description;
    Map<String,String> status;
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
        initializeMission();
    }

    public void initializeMission(){
        this.status = new HashMap<String, String>(){{
            put("发布任务", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            System.out.println(element);
            if (element == 0) {
                put("接稿", "未达成");
                put("写稿", "未达成");
                put("编辑部审稿", "未达成");
            } else {
                put("接稿", "跳过");
                put("写稿", "跳过");
                put("编辑部审稿", "跳过");
            }
            put("辅导员审核", "未达成");
            put("排版", "未达成");
        }};
        reporters = new HashMap<>();
        for (String str : reporterNeeds.keySet()
        ) {
            reporters.put(str, new ArrayList<>());
        }
        // 设置任务号
        if (count < 10) {
            missionID = time.getDataCode() + "0" + count;
        } else {
            missionID = time.getDataCode() + count;
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

    public Map<String, String> getStatus() {
        return status;
    }

    public void setStatus(Map<String, String> status) {
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getElement() {
        return element;
    }

    public void setElement(int element) {
        this.element = element;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "missionID='" + missionID + '\'' +
                ", time=" + time.toString() +
                ", place='" + place + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", reporterNeeds=" + reporterNeeds +
                ", reporters=" + reporters +
                '}';
    }
}
