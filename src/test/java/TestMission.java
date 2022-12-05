import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.client.MongoCollection;
import group.dao.util.DataBaseUtil;
import group.pojo.Mission;
import group.pojo.util.MyTime;
import group.service.ManagerService;
import group.service.impl.ManagerServiceImpl;
import org.bson.Document;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestMission {
    @Test
    public void testAddMission() {

        MongoCollection<Document> missionCollection =
                DataBaseUtil.getMongoDB().getCollection("Mission");

        Mission mission1 = new Mission();
        Mission mission = new Mission(
                new MyTime(2022, 12, 4, 19, 0),
                "东九D216","12月例会","吃蛋糕",new HashMap<String,Integer>(){{put("photo",1);}}
        );

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

        System.out.println("插入完成");
    }

    @Test
    public void testReceiveMission(){
        JSONObject result = new JSONObject();
        String data = "{\"place\": \"111\"," +
                "\"title\": \"222\"," +
                "\"description\": \"333\"," +
                "\"time\": {\"year\": 1234,\"month\": 12,\"day\": 12,\"hour\": 12,\"minute\": 0}," +
                "\"reporterNeeds\": {\"photo\": 1,\"article\": 1}}";

        try {

            JSONObject dataJson = JSONObject.parseObject(data);

            String place = dataJson.getString("place");
            String title = dataJson.getString("title");
            String description = dataJson.getString("description");

            Map<String, Integer> time = JSONObject.parseObject(
                    dataJson.getJSONObject("time").toJSONString(),
                    new TypeReference<Map<String, Integer>>(){});

            Map<String, Integer> reporterNeeds = JSONObject.parseObject(
                    dataJson.getJSONObject("reporterNeeds").toJSONString(),
                    new TypeReference<Map<String, Integer>>(){});

            ManagerService managerService = new ManagerServiceImpl();

            managerService.addMission(new MyTime(time), place, title, description, reporterNeeds);

            result.put("code", 202);
            result.put("msg", "任务添加成功");

        } catch (Exception e) {
            result.put("code", 203);
            result.put("msg", "任务信息错误");
            throw e;
        } finally {
            String resultStr = result.toJSONString();
            System.out.println(resultStr);
        }
    }
}
