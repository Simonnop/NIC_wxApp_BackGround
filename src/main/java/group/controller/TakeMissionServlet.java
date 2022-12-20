package group.controller;

import com.alibaba.fastjson.JSONObject;
import group.controller.exception.*;
import group.pojo.User;
import group.service.UserService;
import group.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@WebServlet("/take")
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
                    throw new AppRuntimeException(ExceptionKind.REQUEST_INFO_ERROR);
            }
        } catch (Exception e) {
            Writer out = resp.getWriter();
            JSONObject result = new JSONObject();

            if (e instanceof AppRuntimeException) {
                result.put("code", ((AppRuntimeException) e).getCode());
                result.put("msg", ((AppRuntimeException) e).getMsg());
            } else {
                result.put("code", 98);
                result.put("msg", "后端TakeMissionServlet处理错误");
            }

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    private void takeMission(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        String data = req.getParameter("data");
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

        String resultStr = result.toJSONString();
        out.write(resultStr);
        out.flush();
        System.out.println(resultStr);

    }

    private void quitMission(HttpServletRequest req, HttpServletResponse resp) {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}