package group.dao.impl;

import com.mongodb.client.MongoCollection;
import group.dao.MissionDao;
import group.dao.util.DataBaseUtil;
import group.pojo.util.MyTime;
import org.bson.Document;

import java.util.Map;

public class MissionDaoImpl implements MissionDao {
    private static final MissionDaoImpl missionDaoImpl = new MissionDaoImpl();
    private MissionDaoImpl(){}

    public static MissionDaoImpl getMissionDao(){
        return missionDaoImpl;
    }

    // 获取集合
    MongoCollection<Document> missionCollection = DataBaseUtil.getMongoDB().getCollection("Mission");

    @Override
    public void add(MyTime time, String place, String title,
                           String description, Map<String, Integer> reporterNeeds) {

    }
}
