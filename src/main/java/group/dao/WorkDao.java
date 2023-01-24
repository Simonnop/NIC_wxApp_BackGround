package group.dao;

import com.mongodb.client.FindIterable;
import group.pojo.WorkFlow;
import org.bson.Document;

public interface WorkDao {

    void addWorkFlow(WorkFlow workFlow);

    FindIterable<Document> showAll();

    <T> FindIterable<Document> searchMissionByInput(String field, T value);

    <T, K> void addToSetInMission(String filterField, T filterValue, String updateField, K updateValue);

    <T, K> void updateInMission(String filterField, T filterValue, String updateField, K updateValue);

    <T> void replaceMission(String filterField, T filterValue, Document document);
}
