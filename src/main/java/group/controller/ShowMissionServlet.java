package group.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import group.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@WebServlet("/show")
public class ShowMissionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String method = req.getParameter("method");
            switch (method) {
                case "showAll":
                    showAllMission(req, resp);
                    break;
                case "showNeed":
                    showNeedMission(req, resp);
                    break;
                case "showByInput":
                    showMissionByInput(req, resp);
                    break;
                default:
                    throw new Exception();
            }
        } catch (Exception e) {
            Writer out = resp.getWriter();
            JSONObject result = new JSONObject();

            result.put("code", 303);
            result.put("msg", "请求信息错误");

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    protected void showAllMission(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        try {
            UserServiceImpl userService = new UserServiceImpl();

            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(userService.showAllMission());
            result.put("data", jsonArray);

            result.put("code", 302);
            result.put("msg", "查询任务成功");

        } catch (Exception e) {
            result.put("code", 300);
            result.put("msg", "后端查询错误");
        } finally {

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    protected void showNeedMission(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        try {
            UserServiceImpl userService = new UserServiceImpl();

            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(userService.showNeedMission());
            result.put("data", jsonArray);

            result.put("code", 302);
            result.put("msg", "查询任务成功");

        } catch (Exception e) {
            result.put("code", 300);
            result.put("msg", "后端查询错误");
        } finally {

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    protected void showMissionByInput(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
