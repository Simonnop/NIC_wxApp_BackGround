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
    public <T> Document searchUserByInput(String field, T value) {
        // 指定查询过滤器
        Bson filter = Filters.eq(field, value);
        // 根据查询过滤器查询
        Document user = userCollection.find(filter).first();
        if (user == null) {
            throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        return user;
    }

    @Override
    public <T, K> void addToSetInUser(String filterField, T filterValue, String updateField, K updateValue) {

        Bson filter = Filters.eq(filterField, filterValue);
        Bson update = Updates.addToSet(updateField, updateValue);

        userCollection.updateOne(filter, update);
    }

}
