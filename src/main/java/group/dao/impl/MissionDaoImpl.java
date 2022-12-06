package group.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import group.dao.MissionDao;
import group.dao.util.DataBaseUtil;
import group.pojo.Mission;
import group.pojo.User;
import group.pojo.util.MyTime;
import org.bson.Document;

import java.util.ArrayList;
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
    public void add(Mission mission) {

        Document document = new Document();

        document.put("missionID", mission.getMissionID());

        JSONObject time = new JSONObject();
        time.put("year", mission.getTime().getYear());
        time.put("month", mission.getTime().getMonth());
        time.put("day", mission.getTime().getDay());
        time.put("hour", mission.getTime().getHour());
        time.put("minute", mission.getTime().getMinute());
        document.put("time", time);

        document.put("place", mission.getPlace());
        document.put("title", mission.getTitle());
        document.put("description", mission.getDescription());
        document.put("status", 0);

        JSONObject reporterNeeds = new JSONObject();
        for (String str : mission.getReporterNeeds().keySet()
        ) {
            reporterNeeds.put(str, mission.getReporterNeeds().get(str));
        }
        document.put("reporterNeeds", reporterNeeds);

        JSONObject reporters = new JSONObject();
        for (String str : mission.getReporters().keySet()
        ) {
            reporters.put(str, new JSONArray());
        }
        document.put("reporters", reporters);

        missionCollection.insertOne(document);
    }

    @Override
    public ArrayList<Document> showAll() {

        ArrayList<Document> missionArray = new ArrayList<>();

        FindIterable<Document> findIterable = missionCollection.find();

        for (Document document : findIterable) {
            document.remove("_id");
            // 计算还缺少的人数
            Document reporterNeeds = (Document) document.get("reporterNeeds");
            Document reporters = (Document) document.get("reporters");
            JSONObject reporterLack = new JSONObject();

            for (String str:reporterNeeds.keySet()
            ) {
                reporterLack.put(str, (Integer) reporterNeeds.get(str) - reporters.getList(str,String.class).size());
            }
            document.put("reporterLack", reporterLack);

            missionArray.add(document);
        }

        return missionArray;
    }

    @Override
    public void showNeed() {

    }

}
