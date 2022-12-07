package group.controller;

import com.alibaba.fastjson.JSONObject;
import group.pojo.User;
import group.service.UserService;
import group.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class TakeMissionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String method = req.getParameter("method");
            switch (method) {
                case "take":
                    takeMission(req, resp);
                    break;
                case "quit":
                    quitMission(req, resp);
                    break;
                default:
                    throw new Exception();
            }
        } catch (Exception e) {
            Writer out = resp.getWriter();
            JSONObject result = new JSONObject();

            result.put("code", 403);
            result.put("msg", "请求信息错误");

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    private void takeMission(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        try {
            String data = req.getParameter("data");
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
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    private void quitMission(HttpServletRequest req, HttpServletResponse resp) {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
