import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import group.dao.util.DataBaseUtil;
import group.service.UserService;
import group.service.manager.UserManager;
import group.service.impl.UserServiceImpl;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

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

    }

    @Test
    public void testLoginByInput() {

        String data = "{\"password\":\"123456\",\"userid\":\"U202116999\"}";

        JSONObject dataJson = JSONObject.parseObject(data);
        JSONObject result = new JSONObject();

        String userid = (String) dataJson.get("userid");
        String password = (String) dataJson.get("password");

        UserService userService = new UserServiceImpl();
        Boolean correctLogin = userService.tryLogin(userid, password);
        Document returnData = UserManager.getUserManager().getUserLoginInfo("userid", userid);

        if (correctLogin) {
            result.put("code", 102);
            result.put("msg", "登录成功");
            result.put("data", returnData);
        } else {
            result.put("code", 101);
            result.put("msg", "密码错误");
        }

        String resultStr = result.toJSONString();
        System.out.println(resultStr);
    }

    @Test
    public void testGetInfo() {
        String username = "test1";

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
            System.out.println("out");
            return;
        }

        System.out.println(singleDocument);

        JSONObject data = new JSONObject();
        data.put("userid", singleDocument.get("userid"));
        data.put("username", singleDocument.get("username"));
        data.put("identity", singleDocument.get("identity"));
        int levelCount = 1;
        for (Integer level :
                singleDocument.getList("authorityLevel", Integer.class)) {
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
