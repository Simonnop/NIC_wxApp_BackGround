package group.service;

import group.pojo.Mission;

public interface ManagerService {
    void addMission(Mission mission);

    void recommendMission(String missionID, String method);
}
