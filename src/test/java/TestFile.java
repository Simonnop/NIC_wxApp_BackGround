import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import group.dao.util.DataBaseUtil;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

public class TestFile {
    MongoCollection<Document> missionCollection =
            DataBaseUtil.getMongoDB().getCollection("Mission");

    @Test
    public void testAddPath(){
        String missionID = "2022120402";
        String filePath = "hi";
        Bson filter = Filters.eq("missionID", missionID);
        Document mission = missionCollection.find(filter).first();

        synchronized (this) {
            if (mission == null) {
                throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
            }

            Bson update = Updates.addToSet("filePath", filePath);
            missionCollection.updateOne(filter, update);
        }
    }
}
