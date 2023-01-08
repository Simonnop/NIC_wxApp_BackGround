package group.dao.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import group.dao.ConfigDao;
import group.dao.util.DataBaseUtil;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ConfigDaoImpl implements ConfigDao {

    private static final ConfigDaoImpl configDaoImpl = new ConfigDaoImpl();

    private ConfigDaoImpl() {
    }

    public static ConfigDaoImpl getConfigDaoImpl() {
        return configDaoImpl;
    }

    // 获取集合
    MongoCollection<Document> ConfigCollection = DataBaseUtil.getMongoDB().getCollection("Config");


    @Override
    public <T> FindIterable<Document> showItemByInput(String field, T value) {
        // 指定查询过滤器
        Bson filter = Filters.eq(field, value);
        // 根据查询过滤器查询
        return ConfigCollection.find(filter);
    }
}
