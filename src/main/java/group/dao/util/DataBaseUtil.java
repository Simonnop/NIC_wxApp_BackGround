package group.dao.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class DataBaseUtil {

    public static MongoDatabase getMongoDB(){


        ConnectionString connectionString = new ConnectionString("mongodb://app:abc123456@ac-7w1zgpn-shard-00-00.4wwsi37.mongodb.net:27017,ac-7w1zgpn-shard-00-01.4wwsi37.mongodb.net:27017,ac-7w1zgpn-shard-00-02.4wwsi37.mongodb.net:27017/?ssl=true&replicaSet=atlas-sggh7w-shard-0&authSource=admin&retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("NIC");

    }
}
