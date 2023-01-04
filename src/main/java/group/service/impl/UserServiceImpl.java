package group.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
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
import java.util.ArrayList;
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
    public JSONObject getUserLoginInfo(String username) {

        Document userInfo = userDao.getUserInfo(username);

        int levelCount = 1;
        for (Integer level : userInfo.getList("authorityLevel", Integer.class)) {
            userInfo.put("authority" + levelCount++, level);
        }

        userInfo.remove("_id");
        userInfo.remove("authorityLevel");
        userInfo.remove("QQ");
        userInfo.remove("tel");
        userInfo.remove("classStr");
        userInfo.remove("password");

        return (JSONObject) JSONObject.toJSON(userInfo);
    }

    @Override
    public JSONArray showAllMission() {
        return new JSONArray() {{
            addAll(changeAndCalculate(missionDao.showAll()));
        }};
    }

    @Override
    public JSONArray showNeedMission() {

        ArrayList<Document> documents = new ArrayList<>();
        // 判断是否缺人
        for (Document document : changeAndCalculate(missionDao.showAll())) {
            if (((Document) document.get("status"))
                    .get("接稿")
                    .equals("未达成")) {
                documents.add(document);
            }
        }
        return new JSONArray() {{
            addAll(documents);
        }};
    }

    @Override
    public JSONArray showMissionById(String missionID) {
        return new JSONArray() {{
            addAll(changeAndCalculate(missionDao.showById(missionID)));
        }};
    }

    @Override
    public void getMission(String username, String missionID, String kind) {

        // 不带事务写法: 不验证 username 响应时间更快

        boolean success = true;
        try {
            missionDao.tryTakeByUser(username, missionID, kind);
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {
            if (success) {
                new Thread(() -> userDao.takeMission(username, missionID)).start();
                new Thread(() -> missionDao.updateStatus(missionID)).start(); // 其实本来就是一个新线程,再包一层为了好读
            }
        }

        // 带事务写法

        /*boolean success = true;

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
        }*/

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

    private ArrayList<Document> changeAndCalculate(FindIterable<Document> documents) {
        // 改成可以 addAll 的格式并计算 缺少人数

        ArrayList<Document> missionArray = new ArrayList<>();

        for (Document document : documents) {
            // 计算还缺少的人数
            missionArray.add(changeAndCalculate(document));
        }

        return missionArray;
    }

    private Document changeAndCalculate(Document document) {

        document.remove("_id");
        // 计算还缺少的人数
        Document reporterNeeds = (Document) document.get("reporterNeeds");
        Document reporters = (Document) document.get("reporters");
        JSONObject reporterLack = new JSONObject();

        for (String str : reporterNeeds.keySet()
        ) {
            reporterLack.put(str, (Integer) reporterNeeds.get(str)
                    - reporters.getList(str, String.class).size());
        }
        document.put("reporterLack", reporterLack);

        return document;
    }
}
