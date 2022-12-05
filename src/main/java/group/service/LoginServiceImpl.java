package group.service;

import group.dao.UserDao;
import group.dao.UserDaoImpl;
import group.pojo.User;

public class LoginServiceImpl implements LoginService {

    UserDao userDao = UserDaoImpl.getUserDao();

    @Override
    public User tryLogin(String username) {

        return userDao.findUser(username);
    }
}
