package group.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import group.dao.util.DataBaseUtil;
import group.pojo.User;
import org.bson.Document;
import org.bson.conversions.Bson;

public class UserDaoImpl implements UserDao {

    @Override
    public User findUser(String username) {

        MongoDatabase mongoDB = DataBaseUtil.getMongoDB();

        // 获取集合
        MongoCollection<Document> userCollection = mongoDB.getCollection("User");
        // 指定查询过滤器
        Bson filter = Filters.eq("username", username);
        // 根据查询过滤器查询
        FindIterable<Document> findIterable = userCollection.find(filter);

        User user = null;

        for (Document document : findIterable) {
            // 遍历结果,但一般只会查出一个
            String password = (String) document.get("password");
            String classStr = (String) document.get("classStr");
            user = new User(username, password, classStr);
            break;
        }

        return user;
    }
}
