package group.dao.impl;

import com.mongodb.client.MongoCollection;
import group.dao.WorkDao;
import group.dao.util.DataBaseUtil;
import group.pojo.WorkFlow;
import group.pojo.util.DocUtil;
import org.bson.Document;

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
}
