package group.service;

import group.pojo.Mission;
import group.pojo.util.MyTime;

import java.util.ArrayList;
import java.util.Map;

public interface ManagerService {
    void addMission(Mission mission);

    void recommendMission(String missionID, String method);
}
