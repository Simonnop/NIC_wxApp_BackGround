package group.service;

import org.apache.commons.fileupload.FileItem;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public interface UserService {

    Boolean tryLogin(String userid, String password);

    ArrayList<Document> showAllMission();

    ArrayList<Document> showNeedMission();

    ArrayList<Document> showMissionById(String missionID);

    ArrayList<Document> showTakenMission(String field, String value);

    void tryGetMission(String userid, String missionID, String kind);

    void uploadFile(List<FileItem> fileItemList, String missionID, String userid, String uploadPath);

    ArrayList<String> showTag(String... str);

    ArrayList<Document> showLessons(String userid, Integer... week);

}
