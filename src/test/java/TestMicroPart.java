import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import group.dao.util.DataBaseUtil;
import group.pojo.part.EnlistPart;
import group.pojo.part.MissionPart;
import group.pojo.util.DocUtil;
import group.pojo.WorkFlow;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TestMicroPart {

    @Test
    public void testAdd() throws Exception {

        MongoCollection<Document> testField = DataBaseUtil.getMongoDB().getCollection("TestField");

        WorkFlow workFlow = new WorkFlow();
        workFlow.setMissionID("12347890");
        workFlow.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        workFlow.setProgressIndex(2);
        workFlow.setParts(new ArrayList<>());

        MissionPart missionPart = new MissionPart();
        missionPart.setPlace("三公");
        missionPart.setTime(new HashMap<String, Integer>() {{
            put("endHour", 11);
            put("month", 12);
            put("year", 2022);
            put("beginHour", 10);
            put("beginMinute", 0);
            put("day", 15);
            put("endMinute", 0);
        }});
        missionPart.setPeopleNeeds(new HashMap<String, Integer>() {{
            put("photo", 2);
            put("article", 2);
        }});
        missionPart.setMissionID(workFlow.getMissionID());
        missionPart.setIndex(1);
        missionPart.setAccordingPartIndex(null);
        missionPart.setDescription("哈哈哈");
        missionPart.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        EnlistPart enlistPart = new EnlistPart();
        enlistPart.setAccordingPartIndex(1);
        enlistPart.setIndex(2);
        enlistPart.setPeopleNeeds(new HashMap<>());
        enlistPart.setPeopleGet(new HashMap<>());
        enlistPart.setMissionID(workFlow.getMissionID());
        enlistPart.setDescription("招人");

        workFlow.getParts().add(DocUtil.obj2Doc(missionPart));
        workFlow.getParts().add(DocUtil.obj2Doc(enlistPart));
        System.out.println(DocUtil.obj2Doc(workFlow));

        testField.insertOne(DocUtil.obj2Doc(workFlow));
    }

    @Test
    public void testUpdate() throws Exception {
        MongoCollection<Document> testField = DataBaseUtil.getMongoDB().getCollection("TestField");
        Document document = testField.find().first();
        WorkFlow workFlow = DocUtil.doc2Obj(document, WorkFlow.class);

        Document document1 = workFlow.getParts().get(1);
        EnlistPart enlistPart = DocUtil.doc2Obj(document1, EnlistPart.class);

        Document document2 = workFlow.getParts().get(enlistPart.getAccordingPartIndex()-1);
        MissionPart missionPart = DocUtil.doc2Obj(document2, MissionPart.class);
        enlistPart.setPeopleNeeds(missionPart.getPeopleNeeds());

        Bson filter = Filters.eq("missionID", enlistPart.getMissionID());
        Bson update;
        if (DocUtil.obj2Doc(enlistPart).get("documentAsMap") == null) {
            update = Updates.set("parts." + (enlistPart.getIndex() - 1),
                    DocUtil.obj2Doc(enlistPart));
        } else {
            update = Updates.set("parts." + (enlistPart.getIndex() - 1),
                    DocUtil.obj2Doc(enlistPart).get("documentAsMap"));
        }

        testField.updateOne(filter, update);
    }

    @Test
    public void testGetMission() throws Exception {
        MongoCollection<Document> testField = DataBaseUtil.getMongoDB().getCollection("TestField");
        Document document = testField.find().first();
        WorkFlow workFlow = DocUtil.doc2Obj(document, WorkFlow.class);

        Document document1 = workFlow.getParts().get(1);
        EnlistPart enlistPart = DocUtil.doc2Obj(document1, EnlistPart.class);

        List<String> list = enlistPart.getPeopleGet().get("photo");
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add("U202111390");
        enlistPart.getPeopleGet().put("photo", list);

        Bson filter = Filters.eq("missionID", enlistPart.getMissionID());
        Bson update;
        if (DocUtil.obj2Doc(enlistPart).get("documentAsMap") == null) {
            update = Updates.set("parts." + (enlistPart.getIndex() - 1),
                    DocUtil.obj2Doc(enlistPart));
        } else {
            update = Updates.set("parts." + (enlistPart.getIndex() - 1),
                    DocUtil.obj2Doc(enlistPart).get("documentAsMap"));
        }
        testField.updateOne(filter, update);
    }

    @Test
    public void testInterface() {
        MongoCollection<Document> testField = DataBaseUtil.getMongoDB().getCollection("TestField");

        WorkFlow workFlow = new WorkFlow();
        workFlow.setMissionID("12347890");
        workFlow.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        workFlow.setProgressIndex(0);
        workFlow.setParts(new ArrayList<>());

        workFlow.getParts().add(DocUtil.obj2Doc(new MissionPart().initPart(workFlow, new JSONObject())));
        workFlow.getParts().add(DocUtil.obj2Doc(new EnlistPart().initPart(workFlow, new JSONObject())));

        workFlow.checkProgress();

        testField.insertOne(DocUtil.obj2Doc(workFlow));
    }
}
