package group.service.manager;

import com.alibaba.fastjson.JSONObject;
import group.dao.MissionDao;
import group.dao.UserDao;
import group.dao.impl.MissionDaoImpl;
import group.dao.impl.UserDaoImpl;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import org.bson.Document;

public class UserManager {
    private static final UserManager userManager = new UserManager();

    private UserManager() {
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    final UserDao userDao = UserDaoImpl.getUserDao();
    final MissionDao missionDao = MissionDaoImpl.getMissionDao();

    public JSONObject getUserLoginInfo(String field, String value) {

        JSONObject userAllInfo = getUserAllInfo(field, value);

        userAllInfo.remove("authorityLevel");
        userAllInfo.remove("QQ");
        userAllInfo.remove("tel");
        userAllInfo.remove("classStr");
        userAllInfo.remove("password");

        return userAllInfo;
    }

    public JSONObject getUserAllInfo(String field, String value) {

        Document userInfo = userDao.searchUserByInput(field, value);
        if (userInfo == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        int levelCount = 1;
        for (Integer level : userInfo.getList("authorityLevel", Integer.class)) {
            userInfo.put("authority" + levelCount++, level);
        }
        userInfo.remove("_id");

        return (JSONObject) JSONObject.toJSON(userInfo);
    }
}
