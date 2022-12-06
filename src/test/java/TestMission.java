import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import group.dao.util.DataBaseUtil;
import group.pojo.Mission;
import group.pojo.util.MyTime;
import group.service.ManagerService;
import group.service.impl.ManagerServiceImpl;
import group.service.impl.UserServiceImpl;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

import java.util.*;

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

    @Test
    public void testQuery(){

        MongoCollection<Document> missionCollection = DataBaseUtil.getMongoDB().getCollection("Mission");


        Bson filter = Filters.eq("time.month", new Date().getMonth()+1);

        System.out.println(new Date().getMonth()+1);

        FindIterable<Document> findIterable = missionCollection.find(filter);

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

            System.out.println(document);

        }
    }

    @Test
    public void testShowAll(){
        JSONObject result = new JSONObject();

        try {
            // TODO 后面再加筛选的条件

            UserServiceImpl userService = new UserServiceImpl();

            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(userService.showAllMission());
            result.put("data", jsonArray);

            result.put("code", 302);
            result.put("msg", "请求显示任务成功");

        } catch (Exception e) {
            result.put("code", 303);
            result.put("msg", "请求显示任务信息错误");
        } finally {

            String resultStr = result.toJSONString();
            System.out.println(resultStr);
        }
    }
}
