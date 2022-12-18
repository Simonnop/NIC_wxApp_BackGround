package group.service;

import group.pojo.Mission;
import group.pojo.util.MyTime;

import java.util.ArrayList;
import java.util.Map;

public interface ManagerService {
    void addMission(MyTime time, String place, String title,
                    String description, Map<String, Integer> reporterNeeds);

    void recommendMission(String missionID, String method);
}
