package group.pojo.part;

import com.alibaba.fastjson.JSONObject;
import group.pojo.WorkFlow;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class MissionPart implements WorkPart {

    // 所属任务号
    String missionID;
    // 顺序
    Integer index;
    // 描述
    String description;
    // 参考任务模块
    Integer accordingPartIndex;
    // 可参与人员
    List<String> accessiblePeople = new ArrayList<>();

    // 地点
    String place;
    // 时间
    Map<String, Integer> time = new HashMap<>();
    // 人员需要
    Map<String, Integer> peopleNeeds = new HashMap<>();


    // 结束情形
    String overKind;
    // 发布人
    String publisher;
    // 模块完成时间
    String FinishTime;

    @Override
    public WorkPart initPart(WorkFlow workFlow, JSONObject dataJson) {
        // 注入属性
        MissionPart missionPart = JSONObject.parseObject(String.valueOf(dataJson), this.getClass());
        // 设置顺序
        missionPart.setIndex(workFlow.getParts().size());
        // 设置任务号
        missionPart.setMissionID(workFlow.getMissionID());
        // 设置类型
        workFlow.getPartsKind().add(missionPart.getIndex(), this.getClass().getSimpleName());

        return missionPart;
    }

    @Override
    public void activePart(WorkFlow workFlow) {
        // 设置任务流的当前任务
        workFlow.setProgressIndex(this.getIndex());
        // 设置参考模块
        this.extendPart(workFlow);
    }

    @Override
    public void extendPart(WorkFlow workFlow) {
    }

    @Override
    public boolean checkFinish(WorkFlow workFlow) {
        setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return true;
    }
}
