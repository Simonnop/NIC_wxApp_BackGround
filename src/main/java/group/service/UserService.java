package group.service;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.fileupload.FileItem;

import java.util.List;

public interface UserService {

    Boolean tryLogin(String userid, String password);

    JSONArray showAllMission();

    JSONArray showNeedMission();

    JSONArray showMissionById(String missionID);

    void tryGetMission(String userid, String missionID, String kind);

    void uploadFile(List<FileItem> fileItemList, String missionID,String uploadPath);

}
