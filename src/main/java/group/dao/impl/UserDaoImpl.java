package group.dao.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import group.dao.UserDao;
import group.dao.util.DataBaseUtil;
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
    public <T> FindIterable<Document> searchUserByInputEqual(String field, T value) {
        // 指定查询过滤器
        Bson filter = Filters.eq(field, value);
        // 根据查询过滤器查询
        return userCollection.find(filter);
    }

    @Override
    public <T> FindIterable<Document> searchUserByInputContain(String field, T value) {
        // 指定查询过滤器
        Bson filter = Filters.elemMatch(field, Filters.eq(value));
        // 根据查询过滤器查询
        return userCollection.find(filter);
    }

    @Override
    public <T, K> void addToSetInUser(String filterField, T filterValue, String updateField, K updateValue) {
        // 集合中插入
        Bson filter = Filters.eq(filterField, filterValue);
        Bson update = Updates.addToSet(updateField, updateValue);

        userCollection.updateOne(filter, update);
    }

    @Override
    public <T, K> void updateInUser(String filterField, T filterValue, String updateField, K updateValue) {
        // 更新字段
        Bson filter = Filters.eq(filterField, filterValue);
        Bson update = Updates.set(updateField, updateValue);

        userCollection.updateOne(filter, update);
    }

}
