package group.service;

import group.pojo.User;
import org.bson.Document;

import java.util.ArrayList;

public interface UserService {

    User tryLogin(String username);

    ArrayList<Document> showAllMission();

    ArrayList<Document> showNeedMission();

    Document showMissionById(String missionID);

    void getMission(String username, String missionID, String kind);

    void likeMission(String missionID); // 对该内容感兴趣

}
