package group.dao;

import com.mongodb.client.FindIterable;
import org.bson.Document;

public interface UserDao {

    // 通过用户名查找用户
    <T> FindIterable<Document> searchUserByInputEqual(String field, T value);

    <T> FindIterable<Document> searchUserByInputContain(String field, T value);

    <T, K> void addToSetInUser(String filterField, T filterValue, String updateField, K updateValue);

    <T, K> void updateInUser(String filterField, T filterValue, String updateField, K updateValue);
}
