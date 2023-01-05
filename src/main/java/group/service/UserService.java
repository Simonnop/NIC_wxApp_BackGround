package group.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;

import java.util.List;

public interface UserService {

    Boolean checkUser(String username,String password);

    JSONObject getUserLoginInfo(String username);

    JSONArray showAllMission();

    JSONArray showNeedMission();

    JSONArray showMissionGotDraft();

    JSONArray showMissionById(String missionID);

    void getMission(String username, String missionID, String kind);

    void likeMission(String missionID); // 对该内容感兴趣

    void uploadFile(List<FileItem> fileItemList, String missionID,String uploadPath);

}
