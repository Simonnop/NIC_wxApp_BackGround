package group.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import group.controller.util.JsonUtil;
import group.pojo.User;
import group.pojo.util.MyTime;
import group.service.ManagerService;
import group.service.UserService;
import group.service.impl.ManagerServiceImpl;
import group.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Objects;

@WebServlet("/manage")
public class ManageMissionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String method = req.getParameter("method");
            switch (method) {
                case "add":
                    addMissionResponse(req, resp);
                    break;
                case "alter":
                    alterMissionResponse(req, resp);
                    break;
                case "delete":
                    deleteMissionResponse(req, resp);
                    break;
                default:
                    throw new Exception();
            }
        } catch (Exception e) {
            Writer out = resp.getWriter();
            JSONObject result = new JSONObject();

            result.put("code", 203);
            result.put("msg", "任务信息错误");

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    protected void addMissionResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        try {
            String data = req.getParameter("data");
            JSONObject dataJson = JSONObject.parseObject(data);

            String place = dataJson.getString("place");
            String title = dataJson.getString("title");
            String description = dataJson.getString("description");
            Map<String, Integer> time = JsonUtil.readStringIntegerJson(dataJson, "time");
            Map<String, Integer> reporterNeeds = JsonUtil.readStringIntegerJson(dataJson, "reporterNeeds");

            ManagerService managerService = new ManagerServiceImpl();
            managerService.addMission(new MyTime(time), place, title, description, reporterNeeds);

            result.put("code", 202);
            result.put("msg", "任务添加成功");

        } catch (Exception e) {
            result.put("code", 203);
            result.put("msg", "任务信息错误");
        } finally {

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    protected void alterMissionResponse(HttpServletRequest req, HttpServletResponse resp){

    }

    protected void deleteMissionResponse(HttpServletRequest req, HttpServletResponse resp){

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
