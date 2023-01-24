package group.dao.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import group.dao.WorkDao;
import group.dao.util.DataBaseUtil;
import group.pojo.WorkFlow;
import group.pojo.util.DocUtil;
import org.bson.Document;
import org.bson.conversions.Bson;

public class WorkDaoImpl implements WorkDao {

    private static final WorkDaoImpl workDao = new WorkDaoImpl();

    private WorkDaoImpl() {
    }

    public static WorkDaoImpl getWorkDaoImpl() {
        return workDao;
    }

    MongoCollection<Document> testField = DataBaseUtil.getMongoDB().getCollection("TestField");

    @Override
    public void addWorkFlow(WorkFlow workFlow) {
        testField.insertOne(DocUtil.obj2Doc(workFlow));
    }

    @Override
    public FindIterable<Document> showAll() {
        return testField.find();
    }

    @Override
    public <T> FindIterable<Document> searchMissionByInput(String field, T value) {
        Bson filter = Filters.eq(field, value);
        return testField.find(filter);
    }

    @Override
    public <T, K> void addToSetInMission(String filterField, T filterValue, String updateField, K updateValue) {
        Bson filter = Filters.eq(filterField, filterValue);
        Bson update = Updates.addToSet(updateField, updateValue);
        testField.updateOne(filter, update);
    }

    @Override
    public <T, K> void updateInMission(String filterField, T filterValue, String updateField, K updateValue) {

        Bson filter = Filters.eq(filterField, filterValue);
        Bson update = Updates.set(updateField, updateValue);
        testField.updateOne(filter, update);
    }

    @Override
    public <T> void replaceMission(String filterField, T filterValue, Document document) {
        Bson filter = Filters.eq(filterField, filterValue);
        testField.findOneAndReplace(filter, document);
    }
}
