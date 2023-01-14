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
public class ExaminePart implements WorkPart {

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
    // 可操作模块
    List<String> activePart = new ArrayList<>();


    // 提交文本  userid + text
    Map<String, String> submitText = new HashMap<>();
    // 提交文件  userid + list<file>
    Map<String, List<String>> submitFile = new HashMap<>();


    // 留言
    Map<String, String> comment = new HashMap<>();
    // 评分
    Map<String, Integer> score = new HashMap<>();
    // 操作 (通过,退回等)
    Map<String, List<String>> action = new HashMap<>();


    // 结束情形
    String overKind;
    // 模块完成时间
    String FinishTime;


    @Override
    public WorkPart initPart(WorkFlow workFlow, JSONObject dataJson) {
        // 注入属性
        ExaminePart examinePart = JSONObject.parseObject(String.valueOf(dataJson), this.getClass());
        // 设置顺序
        examinePart.setIndex(workFlow.getParts().size());
        // 设置任务号
        examinePart.setMissionID(workFlow.getMissionID());
        // 设置类型
        workFlow.getPartsKind().add(examinePart.getIndex(), this.getClass().getSimpleName());

        return examinePart;
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
        Document document = workFlow.getParts().get(accordingPartIndex);
        WorkPart workPart = DocUtil.doc2Obj(document, WorkPart.class);

        if (workPart instanceof SubmitPart) {
            SubmitPart submitPart = (SubmitPart) workPart;
            setSubmitFile(submitPart.getSubmitFile());
            setSubmitText(submitPart.getSubmitText());
        }
    }

    @Override
    public boolean checkFinish(WorkFlow workFlow) {
        if (!(comment == null)) {
            setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            return true;
        } else if (!(score == null)) {
            setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            return true;
        } else if (!(action == null)) {
            setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            return true;
        }
        return false;
    }
}
