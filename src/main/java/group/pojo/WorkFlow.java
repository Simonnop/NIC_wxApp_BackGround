package group.pojo;

import group.pojo.part.*;
import group.pojo.util.DocUtil;
import lombok.Data;
import org.bson.Document;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    Integer progressIndex;
    // 结束时间
    String finishTime;

    public void checkProgress() {
        Document document = parts.get(progressIndex);
        WorkPart workPart = DocUtil.doc2Obj(document, getWorkPartClass(getPartsKind().get(progressIndex)));
        boolean b = workPart.checkFinish(this);
        if (b) {
            if (progressIndex == parts.size()-1) {
                progressIndex = null;
                finishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                return;
            }
            progressIndex++;
            Document documentNext = parts.get(progressIndex);
            WorkPart workPartNext = DocUtil.doc2Obj(documentNext, getWorkPartClass(getPartsKind().get(progressIndex)));
            workPartNext.activePart(this);
            checkProgress();
        }
    }

    public static Class<? extends WorkPart> getWorkPartClass(String clazzName){
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
}
