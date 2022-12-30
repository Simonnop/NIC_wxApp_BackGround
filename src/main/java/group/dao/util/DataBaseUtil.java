package group.dao.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class DataBaseUtil {

    // 饿汉单例,提高加载速度

    private static final MongoDatabase mongoDatabase;
    private static final MongoClient mongoClient;
    private DataBaseUtil(){}

    static{
        ConnectionString connectionString = new ConnectionString("mongodb://1.15.118.125:27017");
        //ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongoClient = MongoClients.create(settings);
        mongoDatabase = mongoClient.getDatabase("NIC");
    }

    public static MongoDatabase getMongoDB(){

        return mongoDatabase;
    }

    public static MongoClient getMongoClient(){

        return mongoClient;
    }
}
