package group.service;

import com.alibaba.fastjson.JSONArray;
import group.pojo.Mission;

public interface ManagerService {

    void addMission(Mission mission);

    JSONArray showMissionGotDraft();

    void recommendMission(String missionID, String method);
}
