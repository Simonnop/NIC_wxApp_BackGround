import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import group.dao.util.DataBaseUtil;
import group.pojo.User;
import group.service.UserService;
import group.service.impl.UserServiceImpl;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

import java.util.Objects;

public class TestLogin {
    @Test
    public void testLogin() {
        //testLoginByInput("test", "123456");
        //testLoginByInput("test", "12345");
        //testLoginByInput("tes", "123456");

        JSONObject result = new JSONObject();
        JSONObject innerResult = new JSONObject();

        innerResult.put("username", "test");
        innerResult.put("password", "123456");

        result.put("method", "signUp");
        result.put("data", innerResult);

        String resultStr = result.toJSONString();

        System.out.println(resultStr);

        testLoginByInput();
        testLoginByInput();

    }

    private static void testLoginByInput(){

        String data = "{\"password\":\"123456\",\"username\":\"test\"}";

        JSONObject dataJson = JSONObject.parseObject(data);

        String username = (String) dataJson.get("username");
        String password = (String) dataJson.get("password");
        String a = dataJson.getString("a");

        UserService loginService = new UserServiceImpl();
        User user = loginService.tryLogin(username);

        JSONObject result = new JSONObject();
        if (user == null) {
            result.put("code", 100);
            result.put("msg", "查无此用户");
        } else if (!Objects.equals(user.getPassword(), password)) {
            result.put("code", 101);
            result.put("msg", "密码错误");
        } else {
            result.put("code", 102);
            result.put("msg", "登录成功");
        }

        String resultStr = result.toJSONString();
        System.out.println(resultStr);
        System.out.println("a is " + a);
    }

    @Test
    public void testGetInfo(){
        String username = "test";

        Bson filter = Filters.eq("username", username);
        // 根据查询过滤器查询
        MongoCollection<Document> userCollection = DataBaseUtil.getMongoDB().getCollection("User");
        FindIterable<Document> findIterable = userCollection.find(filter);

        Document singleDocument = null;

        for (Document document : findIterable) {
            // 遍历结果,但一般只会查出一个
            singleDocument = document;
        }

        if (singleDocument == null) {
            return;
        }

        singleDocument.remove("_id");
        singleDocument.remove("classStr");
        singleDocument.remove("password");
        singleDocument.remove("tel");
        singleDocument.remove("QQ");
        System.out.println(singleDocument);

        JSONObject data = new JSONObject();
        data.put("userid", singleDocument.get("userid"));
        data.put("username", singleDocument.get("username"));
        data.put("identity", singleDocument.get("identity"));
        int levelCount = 1;
        for (Integer level :
                singleDocument.getList("authorityLevel",Integer.class)) {
            data.put("authority" + levelCount++, level);
        }
        data.put("missionTaken", singleDocument.get("missionTaken"));
        JSONArray missionCompleted = new JSONArray();
        for (Document missionDoc :
                singleDocument.getList("missionCompleted", Document.class)) {
            missionCompleted.add(missionDoc.get("missionID"));
        }
        data.put("missionCompleted", missionCompleted);
        System.out.println(data);
    }

}
