package group.pojo;

import org.bson.Document;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class Mission {
    private static int count = 1;

    String missionID;
    int element;  // 任务属性
    Map<String, Integer> time;
    String place;
    String title;
    String publisher;
    String description;
    Map<String,String> status;
    Map<String, Integer> reporterNeeds;
    Map<String, List<User>> reporters;
    ArrayList<String> files;


    public Mission() {
        count++;
    }

    public Mission(Map<String, Integer> time, String place, String title,
                   String description, Map<String, Integer> reporterNeeds) {
        this.time = time;
        this.place = place;
        this.title = title;
        this.description = description;
        this.reporterNeeds = reporterNeeds;
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
            missionID = getDataCode() + "0" + count;
        } else {
            missionID = getDataCode() + count;
        }
        files = new ArrayList<>();
        // count累加
        count++;
        // 控制在两位数内
        if (count == 99) {
            count = 1;
        }
    }

    public Document changeToDocument()  {
        Document doc = new Document();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field :
                fields) {
            field.setAccessible(true);
            try {
                doc.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            doc.remove("count");
        }

        return doc;
    }

    public String getDataCode() {
        String gap1 = "";
        String gap2 = "";
        if (time.get("month") < 10) {
            gap1 = "0";
        }
        if (time.get("day") < 10) {
            gap2 = "0";
        }
        return "" + time.get("year") + gap1 + time.get("month") + gap2 + time.get("day");
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

    public Map<String, Integer> getTime() {
        return time;
    }

    public void setTime(Map<String, Integer> time) {
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
