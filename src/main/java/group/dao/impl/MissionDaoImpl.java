package group.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.dao.MissionDao;
import group.dao.util.DataBaseUtil;
import group.pojo.Mission;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MissionDaoImpl implements MissionDao {
    private static final MissionDaoImpl missionDaoImpl = new MissionDaoImpl();
    private MissionDaoImpl() {
    }
    public static MissionDaoImpl getMissionDao() {
        return missionDaoImpl;
    }
    MongoCollection<Document> missionCollection = DataBaseUtil.getMongoDB().getCollection("Mission");

    @Override
    public void addMission(Mission mission) {
        Document document = mission.changeToDocument();
        missionCollection.insertOne(document);
    }

    @Override
    public FindIterable<Document> showAll() {
        // 查询全部
        return missionCollection.find();
    }

    @Override
    public <T> FindIterable<Document> searchMissionByInput(String field, T value) {
        // 按字段查询
        Bson filter = Filters.eq(field, value);
        return missionCollection.find(filter);
    }

    public <T, K> void addToSetInMission(String filterField, T filterValue, String updateField, K updateValue) {
        // 更新字段(插入)
        Bson filter = Filters.eq(filterField, filterValue);
        Bson update = Updates.addToSet(updateField, updateValue);
        missionCollection.updateOne(filter, update);
    }

    @Override
    public <T, K> void updateInMission(String filterField, T filterValue, String updateField, K updateValue) {
        // 更新字段(插入)
        Bson filter = Filters.eq(filterField, filterValue);
        Bson update = Updates.set(updateField, updateValue);
        missionCollection.updateOne(filter, update);
    }
}
