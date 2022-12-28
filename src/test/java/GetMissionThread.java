import com.alibaba.fastjson.JSONObject;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
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

        String data = "{\"username\":\"test6\",\"missionID\":\"2345121201\",\"kind\":\"article\"}";
        JSONObject result = new JSONObject();

        try {
            JSONObject dataJson = JSONObject.parseObject(data);

            String username = (String) dataJson.get("username");
            String missionID = (String) dataJson.get("missionID");
            String kind = (String) dataJson.get("kind");
            if (username == null || missionID == null || kind == null) {
                throw new AppRuntimeException(ExceptionKind.REQUEST_INFO_ERROR);
            }
            UserService loginService = new UserServiceImpl();
            loginService.getMission(username, missionID, kind);

            result.put("code", 402);
            result.put("msg", "任务参加成功");

        }catch (Exception e) {
            if (e instanceof AppRuntimeException) {
                result.put("code", ((AppRuntimeException) e).getCode());
                result.put("msg", ((AppRuntimeException) e).getMsg());
            } else {
                result.put("code", 98);
                result.put("msg", "后端TakeMissionServlet处理错误");
            }
        } finally {
            String resultStr = result.toJSONString();
            System.out.println(resultStr);
        }
    }
}
