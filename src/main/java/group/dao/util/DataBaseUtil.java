package group.dao.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class DataBaseUtil {

    // 饿汉单例,提高加载速度

    private static final MongoDatabase mongoDatabase;
    private DataBaseUtil(){}

    static{
        ConnectionString connectionString = new ConnectionString("mongodb+srv://app:abc123456@cluster0.4wwsi37.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        mongoDatabase = mongoClient.getDatabase("NIC");
    }

    public static MongoDatabase getMongoDB(){

        return mongoDatabase;
    }
}
