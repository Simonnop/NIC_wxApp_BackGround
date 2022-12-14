package group.service.helper;

import group.dao.MissionDao;
import group.dao.UserDao;
import group.dao.impl.MissionDaoImpl;
import group.dao.impl.UserDaoImpl;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import org.bson.Document;

public class UserHelper {
    private static final UserHelper userHelper = new UserHelper();

    private UserHelper() {
    }

    public static UserHelper getUserHelper() {
        return userHelper;
    }

    final UserDao userDao = UserDaoImpl.getUserDao();
    final MissionDao missionDao = MissionDaoImpl.getMissionDao();

    public Document getUserLoginInfo(String field, String value) {

        Document userAllInfo = getUserAllInfo(field, value);

        userAllInfo.remove("authorityLevel");
        userAllInfo.remove("QQ");
        userAllInfo.remove("tel");
        userAllInfo.remove("classStr");
        userAllInfo.remove("password");

        return userAllInfo;
    }

    public Document getUserAllInfo(String field, String value) {

        Document userInfo = userDao.searchUserByInputEqual(field, value).first();
        if (userInfo == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        int levelCount = 1;
        for (Integer level : userInfo.getList("authorityLevel", Integer.class)) {
            userInfo.put("authority" + levelCount++, level);
        }
        userInfo.remove("_id");

        return userInfo;
    }
}
