package group.dao.impl;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import group.dao.UserDao;
import group.dao.util.DataBaseUtil;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.pojo.User;
import org.bson.Document;
import org.bson.conversions.Bson;

public class UserDaoImpl implements UserDao {

    private static final UserDaoImpl userDaoImpl = new UserDaoImpl();

    private UserDaoImpl() {
    }

    public static UserDaoImpl getUserDao() {
        return userDaoImpl;
    }

    // 获取集合
    MongoCollection<Document> userCollection = DataBaseUtil.getMongoDB().getCollection("User");

    @Override
    public User findUser(String username) {

        // 指定查询过滤器
        Bson filter = Filters.eq("username", username);
        // 根据查询过滤器查询
        Document document = userCollection.find(filter).first();
        if (document == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }

        User user = null;
        String password = (String) document.get("password");
        String classStr = (String) document.get("classStr");
        user = new User(username, password, classStr);

        return user;
    }

    @Override
    public Document getUserInfo(String username) {

        Bson filter = Filters.eq("username", username);
        // 根据查询过滤器查询
        Document returnDoc = userCollection.find(filter).first();
        if (returnDoc == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }

        return returnDoc;
    }

    @Override
    public void takeMission(String username, String missionID) {

        Bson filter = Filters.eq("username", username);
        Document user = userCollection.find(filter).first();
        if (user == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }

        Bson update = Updates.addToSet("missionTaken", missionID);
        userCollection.updateOne(filter, update);
    }
}
