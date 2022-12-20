package group.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import group.controller.exception.AppRuntimeException;
import group.controller.exception.ExceptionKind;
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

    // 获取集合
    MongoCollection<Document> missionCollection = DataBaseUtil.getMongoDB().getCollection("Mission");

    @Override
    public void add(Mission mission) {

        Document document = new Document();

        document.put("missionID", mission.getMissionID());

        JSONObject time = new JSONObject(){{
            put("year", mission.getTime().getYear());
            put("month", mission.getTime().getMonth());
            put("day", mission.getTime().getDay());
            put("beginHour", mission.getTime().getBeginHour());
            put("beginMinute", mission.getTime().getBeginMinute());
            put("endHour", mission.getTime().getEndHour());
            put("endMinute", mission.getTime().getEndMinute());
        }};
        document.put("time", time);

        document.put("place", mission.getPlace());
        document.put("title", mission.getTitle());
        document.put("description", mission.getDescription());

        JSONObject status = new JSONObject(){{
            for (String key : mission.getStatus().keySet()
            ) {
                put(key, mission.getStatus().get(key));
            }
        }};
        document.put("status", status);

        JSONObject reporterNeeds = new JSONObject(){{
            for (String str : mission.getReporterNeeds().keySet()
            ) {
                put(str, mission.getReporterNeeds().get(str));
            }
        }};
        document.put("reporterNeeds", reporterNeeds);

        JSONObject reporters = new JSONObject(){{
            for (String str : mission.getReporters().keySet()
            ) {
                put(str, new JSONArray());
            }
        }};
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

            for (String str : reporterNeeds.keySet()
            ) {
                reporterLack.put(str, (Integer) reporterNeeds.get(str)
                        - reporters.getList(str, String.class).size());
            }
            document.put("reporterLack", reporterLack);

            missionArray.add(document);
        }

        return missionArray;
    }

    @Override
    public ArrayList<Document> showNeed() {

        ArrayList<Document> missionArray = new ArrayList<>();

        FindIterable<Document> findIterable = missionCollection.find();

        for (Document document : findIterable) {
            document.remove("_id");
            // 计算还缺少的人数
            Document reporterNeeds = (Document) document.get("reporterNeeds");
            Document reporters = (Document) document.get("reporters");
            JSONObject reporterLack = new JSONObject();

            int totalNeedCount = 0;
            for (String str : reporterNeeds.keySet()
            ) {
                int needCount = (Integer) reporterNeeds.get(str)
                        - reporters.getList(str, String.class).size();
                totalNeedCount += needCount;
                if (needCount != 0) {
                    reporterLack.put(str, needCount);
                }
            }
            document.put("reporterLack", reporterLack);

            // 如果不需要人了,跳过本次循环
            if (totalNeedCount == 0) {
                continue;
            }
            missionArray.add(document);
        }

        return missionArray;
    }

    @Override
    public Document showById(String missionID) {
        Bson filter = Filters.eq("missionID", missionID);

        Document mission = missionCollection.find(filter).first();
        if (mission == null) {
                throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
        }
        mission.remove("_id");

        return mission;
    }

    @Override
    public void get(String username, String missionID, String kind) {

        Bson filter = Filters.eq("missionID", missionID);

        synchronized (this) {
            Document mission = missionCollection.find(filter).first();
            if (mission == null) {
                throw new AppRuntimeException(ExceptionKind.DATABASE_NOT_FOUND);
            }

            Document reporterNeeds = (Document) mission.get("reporterNeeds");
            Document reporters = (Document) mission.get("reporters");
            if ((Integer) reporterNeeds.get(kind) == reporters.getList(kind, String.class).size()) {
                throw new AppRuntimeException(ExceptionKind.ENOUGH_PEOPLE);
            }

            Bson update = Updates.addToSet("reporters." + kind, username);

            missionCollection.updateOne(filter, update);
        }

        // 判断任务人数是否足够,如果不缺人了就改变状态
        // 采用多线程以挤占响应时间
        new Thread(new Runnable() {
            Document mission = missionCollection.find(filter).first();
            @Override
            public void run() {
                Document reporterNeeds = (Document) mission.get("reporterNeeds");
                Document reporters = (Document) mission.get("reporters");

                for (String str : reporterNeeds.keySet()
                ) {
                    if ((Integer) reporterNeeds.get(str)
                            - reporters.getList(str, String.class).size() != 0) {
                        return;
                    }
                }
                Bson update = Updates.set("status.接稿", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                missionCollection.updateOne(filter, update);
            }
        }).start();
    }
}
