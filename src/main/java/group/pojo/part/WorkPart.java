package group.pojo.part;

import com.alibaba.fastjson.JSONObject;
import group.pojo.WorkFlow;

public interface WorkPart {

     WorkPart initPart(WorkFlow workFlow, JSONObject dataJson);

     void activePart(WorkFlow workFlow);

     void extendPart(WorkFlow workFlow);

     boolean checkFinish(WorkFlow workFlow);
}
