package group.dao.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class DataBaseUtil {

    public static MongoDatabase getMongoDB(){



        ConnectionString connectionString = new ConnectionString("mongodb+srv://app:abc123456@cluster0.4wwsi37.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);

        return mongoClient.getDatabase("NIC");

    }
}
