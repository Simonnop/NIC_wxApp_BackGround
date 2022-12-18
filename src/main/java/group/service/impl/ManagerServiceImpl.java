package group.service.impl;

import group.dao.MissionDao;
import group.dao.impl.MissionDaoImpl;
import group.pojo.Mission;
import group.pojo.util.MyTime;
import group.service.ManagerService;

import java.util.ArrayList;
import java.util.Map;

public class ManagerServiceImpl implements ManagerService {

    MissionDao missionDao = MissionDaoImpl.getMissionDao();

    @Override
    public void addMission(MyTime time, String place, String title,
                           String description, Map<String, Integer> reporterNeeds) {

        Mission mission = new Mission(time, place, title, description, reporterNeeds);
        // 添加任务
        missionDao.add(mission);
    }

    @Override
    public void recommendMission(String missionID, String method) {
        /*
        * TODO 根据 tags 和 用户画像 的 拟合程度 推送
        *  采用 socket 与 python 通信
        * method: 选拟合最好或最差
        * */

    }
}
