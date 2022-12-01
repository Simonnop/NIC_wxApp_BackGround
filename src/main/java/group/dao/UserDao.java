package group.dao;

import group.pojo.User;

public interface UserDao {
    User findUser(String username);
}
