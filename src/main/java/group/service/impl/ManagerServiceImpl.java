package group.service.impl;

import com.mongodb.client.FindIterable;
import group.dao.MissionDao;
import group.dao.impl.MissionDaoImpl;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.pojo.Mission;
import group.service.ManagerService;
import group.service.helper.MissionHelper;
import org.bson.Document;

import java.util.ArrayList;

public class ManagerServiceImpl implements ManagerService {

    final MissionDao missionDao = MissionDaoImpl.getMissionDao();
    final MissionHelper missionManager = MissionHelper.getMissionHelper();

    @Override
    public void addMission(Mission mission) {
        // 初始化任务id与状态
        mission.initializeMission();
        // 添加任务
        missionDao.addMission(mission);
    }

    @Override
    public ArrayList<Document> showMissionGotDraft() {

        FindIterable<Document> documents = missionDao.showAll();
        if (documents.first() == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        ArrayList<Document> documentArrayList = missionManager.changeFormAndCalculate(documents);

        // 判断是否缺人
        documentArrayList.removeIf(document -> ((Document) document
                .get("status"))
                .get("写稿")
                .equals("未达成"));
        return documentArrayList;
    }

    @Override
    public void recommendMission(String missionID, String method) {
        /*
        * TODO
        *  根据 tags 和 用户画像 的 拟合程度 推送
        *  根据 课表情况 推送
        *  采用 socket 与 python 通信
        *
        * */

    }
}
