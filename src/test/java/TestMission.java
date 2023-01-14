import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.dao.util.DataBaseUtil;
import group.pojo.Mission;
import group.service.ManagerService;
import group.service.UserService;
import group.service.impl.ManagerServiceImpl;
import group.service.impl.UserServiceImpl;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

public class TestMission {
    @Test
    public void testAddMission() {

        MongoCollection<Document> missionCollection =
                DataBaseUtil.getMongoDB().getCollection("Mission");

        Mission mission1 = new Mission();
        Mission mission = new Mission(
                new HashMap<String,Integer>(){{
                    put("year",2021);
                    put("month",12);
                    put("day",21);
                    put("beginHour",12);
                    put("beginMinute",21);
                    put("endHour",13);
                    put("endMinute",21);
                }},
                "东九D216", "12月例会", "吃蛋糕", new HashMap<String, Integer>() {{
            put("photo", 1);
        }}
        );


        Document document = mission.changeToDocument();

        System.out.println(document);

        missionCollection.insertOne(document);

        System.out.println("插入完成");
    }

    @Test
    public void testReceiveMission() {
        JSONObject result = new JSONObject();
        String data = "{\"place\": \"这里\"," +
                "\"title\": \"测试新字段\"," +
                "\"publisher\": \"U202116999\"," +
                "\"element\": \"1\"," +
                "\"description\": \"如题\"," +
                "\"time\": {\"year\": 1955,\"month\": 12,\"day\": 12,\"beginHour\": 12,\"beginMinute\": 0,\"endHour\": 13,\"endMinute\": 0}," +
                "\"reporterNeeds\": {\"photo\": 1,\"article\": 1}}";

        System.out.println(data);

        try {
            JSONObject dataJson = JSONObject.parseObject(data);
            dataJson.put("peopleNeeds", dataJson.get("reporterNeeds"));

            ManagerServiceImpl managerService = new ManagerServiceImpl();

            managerService.addMission(dataJson);

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
    public void testQuery() {

        MongoCollection<Document> missionCollection = DataBaseUtil.getMongoDB().getCollection("Mission");


        Bson filter = Filters.eq("time.month", new Date().getMonth() + 1);

        System.out.println(new Date().getMonth() + 1);

        FindIterable<Document> findIterable = missionCollection.find(filter);

        for (Document document : findIterable) {
            document.remove("_id");
            // 计算还缺少的人数
            Document reporterNeeds = (Document) document.get("reporterNeeds");
            Document reporters = (Document) document.get("reporters");
            JSONObject reporterLack = new JSONObject();

            for (String str : reporterNeeds.keySet()
            ) {
                reporterLack.put(str, (Integer) reporterNeeds.get(str) - reporters.getList(str, String.class).size());
            }
            document.put("reporterLack", reporterLack);

            System.out.println(document);

        }
    }

    @Test
    public void testShowAll() {
        JSONObject result = new JSONObject();

        try {
            UserServiceImpl userService = new UserServiceImpl();

            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(userService.showTakenMission("userid","U202116999"));
            //jsonArray.addAll(userService.showAllMission());
            result.put("data", jsonArray);

            result.put("code", 302);
            result.put("msg", "请求显示任务成功");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 303);
            result.put("msg", "请求显示任务信息错误");
        } finally {

            String resultStr = result.toJSONString();
            System.out.println(resultStr);
        }
    }

    @Test
    public void testShowNeed() {
        JSONObject result = new JSONObject();

        try {
            ManagerService managerService = new ManagerServiceImpl();

            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(managerService.showMissionGotDraft());
            //jsonArray.addAll(userService.showNeedMission());
            result.put("data", jsonArray);

            result.put("code", 302);
            result.put("msg", "请求显示任务成功");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 303);
            result.put("msg", "请求显示任务信息错误");
        } finally {

            String resultStr = result.toJSONString();
            System.out.println(resultStr);
        }
    }

    public static void doubleGet(){

        testGetMission();
        System.out.println("new request");
        testGetMission();
    }

    public static void main(String[] args) {

        testGetMission();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
        //doubleGet();
    }

    // get现在是多线程方法,不能用 Junit 测试
    public static void testGetMission() {

        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));

        String data = "{\"username\":\"test\",\"missionID\":\"2022123050\",\"kind\":\"article\"}";
        JSONObject result = new JSONObject();

        try {
            JSONObject dataJson = JSONObject.parseObject(data);

            String username = (String) dataJson.get("username");
            String missionID = (String) dataJson.get("missionID");
            String kind = (String) dataJson.get("kind");
            if (username == null || missionID == null || kind == null) {
                throw new AppRuntimeException(ExceptionKind.REQUEST_INFO_ERROR);
            }
            UserService userService = new UserServiceImpl();
            userService.tryGetMission(username, missionID, kind);

            result.put("code", 402);
            result.put("msg", "任务参加成功");

        }catch (Exception e) {
            e.printStackTrace();
            if (e instanceof AppRuntimeException) {
                result.put("code", ((AppRuntimeException) e).getCode());
                result.put("msg", ((AppRuntimeException) e).getMsg());
            } else {
                result.put("code", 98);
                result.put("msg", "后端LoginServlet处理错误");
            }
        } finally {
            String resultStr = result.toJSONString();
            System.out.println(resultStr);
        }
    }

    @Test
    public void testShowMissionById(){
        JSONObject result = new JSONObject();

        String data = "{\"missionID\":\"2345121201\"}";
        JSONObject dataJson = JSONObject.parseObject(data);
        String missionID = (String) dataJson.get("missionID");

        UserServiceImpl userService = new UserServiceImpl();

        result.put("data", userService.showMissionById(missionID));

        result.put("code", 302);
        result.put("msg", "指定查询任务成功");

        String resultStr = result.toJSONString();
        System.out.println(resultStr);
    }


}
