import com.alibaba.fastjson.JSONObject;
import group.service.UserService;
import group.service.impl.UserServiceImpl;

import java.util.Date;

public class GetMissionThread extends Thread {
    @Override
    public void run() {
        testGetMission();
    }

    public static void main(String[] args) {
        new GetMissionThread().start();
        new GetMissionThread().start();
    }

    public void testGetMission() {

        System.out.println(new Date());

        String data = "{\"username\":\"test1\",\"missionID\":\"1234121201\",\"kind\":\"photo\"}";
        JSONObject result = new JSONObject();

        try {
            JSONObject dataJson = JSONObject.parseObject(data);

            String username = (String) dataJson.get("username");
            String missionID = (String) dataJson.get("missionID");
            String kind = (String) dataJson.get("kind");
            if (username == null || missionID == null || kind == null) {
                throw new Exception();
            }
            UserService loginService = new UserServiceImpl();
            loginService.getMission(username, missionID, kind);

            result.put("code", 402);
            result.put("msg", "任务参加成功");

        } catch (RuntimeException e) {
            result.put("code", 401);
            result.put("msg", "需要人数已满");
        } catch (Exception e) {
            result.put("code", 403);
            result.put("msg", "请求信息错误");
        } finally {
            String resultStr = result.toJSONString();
            System.out.println(resultStr);
        }
    }
}
