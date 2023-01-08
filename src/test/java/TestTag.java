import com.alibaba.fastjson.JSONObject;
import group.service.UserService;
import group.service.impl.UserServiceImpl;
import group.service.manager.UserManager;
import org.bson.Document;
import org.junit.Test;

import java.util.ArrayList;

public class TestTag {
    @Test
    public void testTag() {
        // String data = "{\"firstTag\":\"tag1\"}";
        String data = null;

        String firstTag = null;
        JSONObject result = new JSONObject();

        try {
            JSONObject dataJson = JSONObject.parseObject(data);
            firstTag = (String) dataJson.get("firstTag");
        } catch (Exception e) {

        }

        UserService userService = new UserServiceImpl();

        ArrayList<String> tags;
        if (firstTag == null) {
            tags = userService.showTag();
        } else {
            tags = userService.showTag(firstTag);
            System.out.println(tags);
        }

        result.put("code", 202);
        result.put("msg", "tag查询成功");
        result.put("data", tags);

        String resultStr = result.toJSONString();
        System.out.println(resultStr);
    }
}
