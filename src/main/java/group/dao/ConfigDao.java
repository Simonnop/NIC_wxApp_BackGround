package group.dao;

import com.mongodb.client.FindIterable;
import org.bson.Document;

public interface ConfigDao {

    <T> FindIterable<Document> showItemByInput(String field, T value);
}
