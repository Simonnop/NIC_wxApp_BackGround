package group.dao;

import org.bson.Document;

public interface UserDao {

    // 通过用户名查找用户
    <T> Document searchUserByInputEqual(String field, T value);

    <T> Document searchUserByInputContain(String field, T value);

    <T, K> void addToSetInUser(String filterField, T filterValue, String updateField, K updateValue);
}
