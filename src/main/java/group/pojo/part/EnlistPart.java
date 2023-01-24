package group.pojo.part;

import com.alibaba.fastjson.JSONObject;
import group.pojo.WorkFlow;
import group.pojo.util.DocUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class EnlistPart implements WorkPart{

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


    // 招募到的人员
    Map<String, List<String>> peopleGet = new HashMap<>();
    // 人员需要
    Map<String, Integer> peopleNeeds = new HashMap<>();


    // 结束情形
    String overKind;
    // 模块完成时间
    String FinishTime;

    @Override
    public WorkPart initPart(WorkFlow workFlow, JSONObject dataJson) {

        EnlistPart enlistPart;
        // 注入属性
        if (dataJson == null) {
            enlistPart = this;
        } else {
            enlistPart = JSONObject.parseObject(String.valueOf(dataJson), this.getClass());
        }
        // 设置顺序
        enlistPart.setIndex(workFlow.getParts().size());
        // 设置任务号
        enlistPart.setMissionID(workFlow.getMissionID());
        // 设置类型
        workFlow.getPartsKind().add(enlistPart.getIndex(), this.getClass().getSimpleName());

        return enlistPart;
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
        // 拿出参考模块
        Integer accordingPartIndex = this.getAccordingPartIndex();
        if (accordingPartIndex == null) {
            return;
        }
        WorkPart workPart = workFlow.getWorkPart(accordingPartIndex);

        if (workPart instanceof MissionPart) {
            MissionPart missionPart = (MissionPart) workPart;
            System.out.println(missionPart);
            // 继承需要人数
            this.setPeopleNeeds(missionPart.getPeopleNeeds());
            for (String s : getPeopleNeeds().keySet()) {
                this.getPeopleGet().put(s, new ArrayList<>());
            }
        }
        workFlow.setWorkPart(index, this);
    }

    @Override
    public boolean checkFinish(WorkFlow workFlow) {
        for (String s : getPeopleNeeds().keySet()) {
            if (!(getPeopleGet().get(s).size() == getPeopleNeeds().get(s))) {
                return false;
            }
        }
        setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        workFlow.setWorkPart(index, this);
        return true;
    }
}
