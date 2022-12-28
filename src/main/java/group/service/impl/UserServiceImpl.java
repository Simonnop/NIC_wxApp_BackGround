package group.service.impl;

import group.dao.MissionDao;
import group.dao.UserDao;
import group.dao.impl.MissionDaoImpl;
import group.dao.impl.UserDaoImpl;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.pojo.User;
import group.service.UserService;
import org.apache.commons.fileupload.FileItem;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    UserDao userDao = UserDaoImpl.getUserDao();
    MissionDao missionDao = MissionDaoImpl.getMissionDao();

    @Override
    public User tryLogin(String username) {

        return userDao.findUser(username);
    }

    @Override
    public ArrayList<Document> showAllMission() {

        return missionDao.showAll();
    }

    @Override
    public ArrayList<Document> showNeedMission() {

        return missionDao.showNeed();
    }

    @Override
    public Document showMissionById(String missionID) {

        return missionDao.showById(missionID);
    }

    @Override
    public void getMission(String username, String missionID, String kind) {

        missionDao.get(username, missionID, kind);
        // 更新任务状态(新线程)
        missionDao.updateStatus(missionID);
    }

    @Override
    public void likeMission(String missionID) {

        /*
        * TODO 标记感兴趣的任务, 存储在用户画像中
        * */
    }

    @Override
    public void uploadFile(List<FileItem> formItems, String missionID,String uploadPath) {
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
                    String fileName = new File(item.getName()).getName();
                    String filePath = uploadPath + File.separator + fileName;
                    File storeFile = new File(filePath);
                    // 在控制台输出文件的上传路径
                    System.out.println(filePath);
                    // 保存文件到硬盘
                    try {
                        item.write(storeFile);
                    } catch (Exception e) {
                        throw new AppRuntimeException(ExceptionKind.FILE_SAVE_ERROR);
                    }
                    // 将文件名保存到对应的任务下
                    missionDao.updateFilePath(fileName, missionID);
                }
            }
        }
    }
}
