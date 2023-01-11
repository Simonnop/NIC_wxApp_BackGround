package group.service;

import group.pojo.Mission;
import org.bson.Document;

import java.util.ArrayList;

public interface ManagerService {

    void addMission(Mission mission);

    ArrayList<Document> showMissionGotDraft();

    ArrayList<String> findAvailableReporters(String missionID, Integer... intervals);

    void recommendMission(String missionID, String method);
}
