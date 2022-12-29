package group.dao;

import group.pojo.User;
import org.bson.Document;

public interface UserDao {

    // 通过用户名查找用户
    User findUser(String username);

    Document getUserInfo(String username);
}
