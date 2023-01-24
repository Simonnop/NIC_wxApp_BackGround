package group.pojo;

import group.pojo.part.*;
import group.pojo.util.DocUtil;
import lombok.Data;
import org.bson.Document;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class WorkFlow {

    // 任务号
    String missionID;
    // 任务开始时间
    String startTime;

    // 模块内容
    List<String> partsKind = new ArrayList<>();
    List<Document> parts = new ArrayList<>();
    // 任务进度
    Integer progressIndex = 0;
    // 结束时间
    String finishTime;

    public WorkFlow() {
        startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        missionID = this.initMissionID();
    }

    public void checkProgress() {
        WorkPart workPart = this.getCurrentWorkPart();
        boolean b = workPart.checkFinish(this);
        if (b) {
            if (progressIndex == parts.size() - 1) {
                progressIndex = null;
                finishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                return;
            }
            progressIndex++;
            WorkPart workPartNext = this.getCurrentWorkPart();
            workPartNext.activePart(this);
            checkProgress();
        }
    }

    public static Class<? extends WorkPart> getWorkPartClass(String clazzName) {
        switch (clazzName) {
            case "EnlistPart":
                return EnlistPart.class;
            case "MissionPart":
                return MissionPart.class;
            case "SubmitPart":
                return SubmitPart.class;
            case "ExaminePart":
                return ExaminePart.class;
        }
        return null;
    }

    public void insertWorkPart(WorkPart workPart) {
        this.getParts().add(DocUtil.obj2Doc(workPart));
    }

    public void setWorkPart(int index, WorkPart workPart) {
        this.getParts().set(index, DocUtil.obj2Doc(workPart));
    }

    public WorkPart getCurrentWorkPart() {
        return DocUtil.doc2Obj(
                parts.get(progressIndex),
                getWorkPartClass(getPartsKind().get(progressIndex))
        );
    }

    public WorkPart getWorkPart(int index) {
        return DocUtil.doc2Obj(
                parts.get(index),
                getWorkPartClass(getPartsKind().get(index))
        );
    }

    public String initMissionID() {
        Map<String, Integer> time;
        String gap1 = "";
        String gap2 = "";

        Calendar calendar = Calendar.getInstance();
        time = new HashMap<>();
        time.put("year", calendar.get(Calendar.YEAR));
        time.put("month", calendar.get(Calendar.MONTH) + 1);
        time.put("day", calendar.get(Calendar.DATE));

        if (time.get("month") < 10) {
            gap1 = "0";
        }
        if (time.get("day") < 10) {
            gap2 = "0";
        }
        return "" + time.get("year") + gap1 + time.get("month") + gap2 + time.get("day") +
               new Random().nextInt(10) + new Random().nextInt(10) + new Random().nextInt(10);
    }
}
