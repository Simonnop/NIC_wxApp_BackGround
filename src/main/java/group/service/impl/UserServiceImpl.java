package group.service.impl;

import com.mongodb.client.FindIterable;
import group.dao.ConfigDao;
import group.dao.LessonDao;
import group.dao.MissionDao;
import group.dao.UserDao;
import group.dao.impl.ConfigDaoImpl;
import group.dao.impl.LessonDaoImpl;
import group.dao.impl.MissionDaoImpl;
import group.dao.impl.UserDaoImpl;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.service.UserService;
import group.service.helper.MissionHelper;
import group.service.util.TimeUtil;
import org.apache.commons.fileupload.FileItem;
import org.bson.Document;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserServiceImpl implements UserService {

    final UserDao userDao = UserDaoImpl.getUserDao();
    final MissionDao missionDao = MissionDaoImpl.getMissionDao();
    final ConfigDao configDao = ConfigDaoImpl.getConfigDaoImpl();
    final LessonDao lessonDao = LessonDaoImpl.getLessonDao();
    final MissionHelper missionManager = MissionHelper.getMissionHelper();

    /*
    MongoClient mongoClient = DataBaseUtil.getMongoClient();
    TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary())
            .readConcern(ReadConcern.LOCAL)
            .writeConcern(WriteConcern.MAJORITY)
            .build();
    */

    @Override
    public Boolean tryLogin(String userid, String password) {

        Document user = userDao.searchUserByInputEqual("userid", userid).first();
        if (user == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        return password.equals(user.get("password"));
    }

    @Override
    public ArrayList<Document> showAllMission() {

        FindIterable<Document> documents = missionDao.showAll();
        if (documents.first() == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        return missionManager.changeFormAndCalculate(documents);
    }

    @Override
    public ArrayList<Document> showNeedMission() {

        ArrayList<Document> documentArrayList = new ArrayList<>();

        FindIterable<Document> documents = missionDao.showAll();
        if (documents.first() == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        // ??????????????????
        for (Document document : missionManager.changeFormAndCalculate(documents)) {
            if (((Document) document.get("status"))
                    .get("??????")
                    .equals("?????????")) {
                documentArrayList.add(document);
            }
        }
        return documentArrayList;

    }

    @Override
    public ArrayList<Document> showMissionById(String missionID) {

        FindIterable<Document> documents = missionDao.searchMissionByInput("missionID", missionID);
        if (documents.first() == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        return missionManager.changeFormAndCalculate(documents);

    }

    @Override
    public ArrayList<Document> showTakenMission(String field, String value) {

        ArrayList<Document> documentArrayList = new ArrayList<>();

        Document userInfo = userDao.searchUserByInputEqual(field, value).first();
        if (userInfo == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        for (String missionID : userInfo.getList("missionTaken", String.class)) {
            Document document = missionDao.searchMissionByInput("missionID", missionID).first();
            if (document == null) {
                continue;
            }
            if (((Document) document.get("status"))
                    .get("??????")
                    .equals("?????????")) {
                documentArrayList.add(missionManager.calculateLack(document));
                System.out.println(missionID);
            }
        }
        return documentArrayList;
    }

    @Override
    public void tryGetMission(String userid, String missionID, String kind) {

        // ??????????????????: ????????? username ??????????????????
        boolean success = true;
        try {
            synchronized (missionDao) {
                Document document = missionDao.searchMissionByInput("missionID", missionID).first();
                // ????????????
                if (document == null) {
                    throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
                }
                missionManager.calculateLack(document);
                // ???????????????
                if (((Document) document.get("reporterLack"))
                        .get(kind)
                        .equals(0)) {
                    throw new AppRuntimeException(ExceptionKind.ENOUGH_PEOPLE);
                }
                // ???????????????
                if (((ArrayList<?>) ((Document) document.get("reporters"))
                        .get(kind))
                        .contains(userid)) {
                    throw new AppRuntimeException(ExceptionKind.ALREADY_PARTICIPATE);
                }
                missionDao.addToSetInMission("missionID", missionID, "reporters." + kind, userid);
            }
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {
            if (success) {
                new Thread(() -> userDao.addToSetInUser(
                        "userid", userid, "missionTaken", missionID))
                        .start();
                new Thread(() -> missionManager.updateMissionStatus(missionID)).start();
            }
        }

        // ???????????????
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
    public void uploadFile(List<FileItem> formItems, String missionID, String userid,String uploadPath) {
        // ??????????????????????????????
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        if (formItems != null && formItems.size() > 0) {
            // ??????????????????
            for (FileItem item : formItems) {
                // ??????????????????????????????
                if (!item.isFormField()) {
                    String fileName = new File(missionID + "_" + item.getName()).getName();
                    String filePath = uploadPath + File.separator + fileName;
                    File storeFile = new File(filePath);
                    try {
                        // ?????????????????????
                        item.write(storeFile);
                    } catch (Exception e) {
                        throw new AppRuntimeException(ExceptionKind.SAME_FILE_ERROR);
                    }
                    new Thread(() -> {
                        // ???????????????????????????????????????
                        missionDao.addToSetInMission(
                                "missionID", missionID,
                                "files", fileName);
                        // ??????????????????
                        missionDao.updateInMission(
                                "missionID", missionID,
                                "status.??????",
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        // ?????????????????????
                        missionDao.updateInMission(
                                "missionID", missionID,
                                "statusChanger.??????",
                                userDao.searchUserByInputEqual("userid",userid)
                                        .first()
                                        .get("username"));
                        // ??? missionID ?????? user ??? missionCompleted ???
                        /*for (Document document : userDao.searchUserByInputContain("missionTaken", missionID)) {
                            userDao.addToSetInUser(
                                    "userid", document.get("userid"),
                                    "missionCompleted", missionID);
                        }*/
                    }).start();
                }
            }
        }
    }

    @Override
    public ArrayList<String> showTag(String... str) {
        Document document = configDao.showItemByInput("item", "tag").first();
        if (str.length == 0) {
            return (ArrayList<String>) document.getList("firstLayer", String.class);
        } else {
            return (ArrayList<String>) document.getList(str[0], String.class);
        }
    }

    @Override
    public ArrayList<Document> showLessons(String userid, Integer... week) {

        ArrayList<Document> documents = lessonDao.showLessonsByInput("userid", userid);

        for (Document document : documents) {
            document.put("season", TimeUtil.getSeason((Integer) document.get("week")));
        }
        return documents;
    }
}
