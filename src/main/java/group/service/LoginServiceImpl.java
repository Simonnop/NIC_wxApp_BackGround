package group.service;

import group.dao.UserDao;
import group.dao.UserDaoImpl;
import group.pojo.User;

public class LoginServiceImpl implements LoginService {
    @Override
    public User tryLogin(String username) {

        UserDao userDao = new UserDaoImpl();

        return userDao.findUser(username);
    }
}
