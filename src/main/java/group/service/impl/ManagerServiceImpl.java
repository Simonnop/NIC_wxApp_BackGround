package group.service.impl;

import group.dao.MissionDao;
import group.dao.impl.MissionDaoImpl;
import group.pojo.Mission;
import group.service.ManagerService;

public class ManagerServiceImpl implements ManagerService {

    final MissionDao missionDao = MissionDaoImpl.getMissionDao();

    @Override
    public void addMission(Mission mission) {
        // 初始化任务id与状态
        mission.initializeMission();
        // 添加任务
        missionDao.addMission(mission);
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
