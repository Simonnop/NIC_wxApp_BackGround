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

    // 获取集合
    MongoCollection<Document> missionCollection = DataBaseUtil.getMongoDB().getCollection("Mission");

    @Override
    public void addMission(Mission mission) {

        Document document = mission.changeToDocument();

        missionCollection.insertOne(document);
    }

    @Override
    public FindIterable<Document> showAll() {

        return missionCollection.find();
    }

    @Override
    public <T> FindIterable<Document> searchMissionByInput(String field, T value) {

        Bson filter = Filters.eq(field, value);

        return missionCollection.find(filter);
    }

    public <T, K> void addToSetInMission(String filterField, T filterValue, String updateField, K updateValue) {

        Bson filter = Filters.eq(filterField, filterValue);
        Bson update = Updates.addToSet(updateField, updateValue);

        missionCollection.updateOne(filter, update);
    }

    @Override
    public void updateFilePath(String filePath, String missionID) {
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

    @Override
    public void updateStatus(String missionID) {

        new Thread(new Runnable() {
            // 判断任务人数是否足够,如果不缺人了就改变状态
            // 采用多线程以挤占响应时间
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                    // 延迟5s等待添加成功
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Bson filter = Filters.eq("missionID", missionID);
                Document mission = missionCollection.find(filter).first();

                Document reporterNeeds = (Document) mission.get("reporterNeeds");
                Document reporters = (Document) mission.get("reporters");

                Bson update = Updates.set("status.接稿", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                for (String str : reporterNeeds.keySet()
                ) {
                    if ((Integer) reporterNeeds.get(str)
                            - reporters.getList(str, String.class).size() != 0) {
                        update = Updates.set("status.接稿", "未达成");
                        break;
                    }
                }

                missionCollection.updateOne(filter, update);

                System.out.println("updated");
            }
        }).start();
    }

}
