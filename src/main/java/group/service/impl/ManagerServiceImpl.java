package group.service.impl;

import group.dao.MissionDao;
import group.dao.impl.MissionDaoImpl;
import group.pojo.Mission;
import group.pojo.util.MyTime;
import group.service.ManagerService;

import java.util.Map;

public class ManagerServiceImpl implements ManagerService {

    MissionDao missionDao = MissionDaoImpl.getMissionDao();

    @Override
    public void addMission(MyTime time, String place, String title,
                           String description, Map<String, Integer> reporterNeeds) {

        Mission mission = new Mission(time, place, title, description, reporterNeeds);

        missionDao.add(mission);
    }
}
