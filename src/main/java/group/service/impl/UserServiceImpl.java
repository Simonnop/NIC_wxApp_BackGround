package group.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import group.dao.MissionDao;
import group.dao.UserDao;
import group.dao.impl.MissionDaoImpl;
import group.dao.impl.UserDaoImpl;
import group.dao.util.DataBaseUtil;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.service.UserService;
import org.apache.commons.fileupload.FileItem;
import org.bson.Document;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class UserServiceImpl implements UserService {

    UserDao userDao = UserDaoImpl.getUserDao();
    MissionDao missionDao = MissionDaoImpl.getMissionDao();
    MongoClient mongoClient = DataBaseUtil.getMongoClient();
    TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary())
            .readConcern(ReadConcern.LOCAL)
            .writeConcern(WriteConcern.MAJORITY)
            .build();

    @Override
    public Boolean checkUser(String username, String password) {

        return Objects.equals(
                userDao.findUser(username)
                        .getPassword(),
                password);
    }

    @Override
    public JSONObject getUserInfo(String username) {

        JSONObject data = new JSONObject();

        Document userInfo = userDao.getUserInfo(username);

        data.put("userid", userInfo.get("userid"));
        data.put("username", userInfo.get("username"));
        data.put("identity", userInfo.get("identity"));
        data.put("missionTaken", userInfo.get("missionTaken"));

        int levelCount = 1;
        for (Integer level : userInfo.getList("authorityLevel", Integer.class)) {
            data.put("authority" + levelCount++, level);
        }

        JSONArray missionCompleted = new JSONArray();
        for (Document missionDoc : userInfo.getList("missionCompleted", Document.class)) {
            missionCompleted.add(missionDoc.get("missionID"));
        }
        data.put("missionCompleted", missionCompleted);

        return data;
    }

    @Override
    public JSONArray showAllMission() {
        return new JSONArray() {{
            addAll(missionDao.showAll());
        }};
    }

    @Override
    public JSONArray showNeedMission() {
        return new JSONArray() {{
            addAll(missionDao.showNeed());
        }};
    }

    @Override
    public JSONArray showMissionById(String missionID) {
        return new JSONArray() {{
            add(missionDao.showById(missionID));
        }};
    }

    @Override
    public void getMission(String username, String missionID, String kind) {

        boolean success = true;

        ClientSession clientSession = mongoClient.startSession();
        try {
            clientSession.startTransaction(txnOptions);

            missionDao.tryTakeByUser(username, missionID, kind, clientSession);
            userDao.takeMission(username, missionID, clientSession);

            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
            success = false;
            throw e;
        } finally {
            if (success) {
                missionDao.updateStatus(missionID);
            }
            clientSession.close();
        }

    }

    @Override
    public void likeMission(String missionID) {

        /*
         * TODO 标记感兴趣的任务, 存储在用户画像中
         * */
    }

    @Override
    public void uploadFile(List<FileItem> formItems, String missionID, String uploadPath) {
        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        if (formItems != null && formItems.size() > 0) {
            // 迭代表单数据
            for (FileItem item : formItems) {
                // 处理不在表单中的字段
                if (!item.isFormField()) {
                    String fileName = new File(missionID + "_" + item.getName()).getName();
                    String filePath = uploadPath + File.separator + fileName;
                    File storeFile = new File(filePath);
                    try {
                        // 保存文件到硬盘
                        item.write(storeFile);
                    } catch (Exception e) {
                        throw new AppRuntimeException(ExceptionKind.SAME_FILE_ERROR);
                    }
                    try {
                        // 将文件名保存到对应的任务下
                        missionDao.updateFilePath(fileName, missionID);
                    } catch (Exception e) {
                        storeFile.delete();
                        throw e;
                    }
                }
            }
        }
    }
}
