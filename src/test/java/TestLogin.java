import com.alibaba.fastjson.JSONObject;
import group.pojo.User;
import group.service.UserService;
import group.service.UserServiceImpl;

import java.util.Objects;

public class TestLogin {
    public static void main(String[] args) {
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
        testLoginByWrongInput();
        testLoginByInput();

    }

    private static void testLoginByInput(){

        String data = "{\"password\":\"123456\",\"username\":\"test\"}";

        JSONObject dataJson = JSONObject.parseObject(data);

        String username = (String) dataJson.get("username");
        String password = (String) dataJson.get("password");

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
    }


    private static void testLoginByWrongInput(){

        String method = "Test";

        switch (method) {
            case "signUp":
                break;
            case "signIn":
                break;
            case "tourist":
                break;
            default:
                JSONObject result = new JSONObject();
                result.put("code", 103);
                result.put("msg", "登录信息错误");
                String resultStr = result.toJSONString();
                System.out.println(resultStr);
                break;
        }
    }
}
