package group.dao.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class DataBaseUtil {

    // 饿汉单例,提高加载速度

    private static MongoDatabase mongoDatabase;
    private static MongoClient mongoClient;
    private DataBaseUtil(){}

    static{
        try {
            ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            mongoClient = MongoClients.create(settings);
            mongoDatabase = mongoClient.getDatabase("NIC");
            Document test = mongoDatabase.getCollection("User")
                    .find()
                    .first();
            if (test == null) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            System.out.println("change");
            ConnectionString connectionString = new ConnectionString("mongodb://1.15.118.125:27017");
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            mongoClient = MongoClients.create(settings);
            mongoDatabase = mongoClient.getDatabase("NIC");
        }
    }

    public static MongoDatabase getMongoDB(){

        return mongoDatabase;
    }

    public static MongoClient getMongoClient(){

        return mongoClient;
    }
}
