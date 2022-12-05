package group.service.impl;

import group.dao.MissionDao;
import group.dao.UserDao;
import group.dao.impl.MissionDaoImpl;
import group.dao.impl.UserDaoImpl;
import group.pojo.User;
import group.service.UserService;

public class UserServiceImpl implements UserService {

    UserDao userDao = UserDaoImpl.getUserDao();
    MissionDao missionDao = MissionDaoImpl.getMissionDao();

    @Override
    public User tryLogin(String username) {

        return userDao.findUser(username);
    }

    @Override
    public void getMission(String username, String missionID, String kind) {

    }
}
