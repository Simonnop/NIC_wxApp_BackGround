package group.dao;

import com.mongodb.client.ClientSession;
import group.pojo.User;
import org.bson.Document;

public interface UserDao {

    // 通过用户名查找用户
    User findUser(String username);

    Document getUserInfo(String username);

    void takeMission(String username, String missionID, ClientSession clientSession);
}
