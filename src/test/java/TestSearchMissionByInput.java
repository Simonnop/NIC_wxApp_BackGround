import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import group.dao.util.DataBaseUtil;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

import java.util.Date;

public class TestSearchMissionByInput {
    @Test
    public void testSearchFunc() {
        SearchMissionByInput("missionID","2345121201");
    }

    public <T> void SearchMissionByInput(String field, T value){

        MongoCollection<Document> missionCollection =
                DataBaseUtil.getMongoDB().getCollection("Mission");

        Bson filter = Filters.eq(field, value);

        FindIterable<Document> findIterable = missionCollection.find(filter);

        for (Document doc :
                findIterable) {
            System.out.println(doc);
        }
    }
}
