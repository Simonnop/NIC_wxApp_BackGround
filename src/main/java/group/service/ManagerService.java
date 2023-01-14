package group.service;

import com.alibaba.fastjson.JSONObject;
import group.pojo.Mission;
import org.bson.Document;

import java.util.ArrayList;

public interface ManagerService {

    void addMission(JSONObject jsonObject);

    ArrayList<Document> showMissionGotDraft();

    ArrayList<String> findAvailableReporters(String missionID, Integer... intervals);

    void recommendMission(String missionID, String method);
}
