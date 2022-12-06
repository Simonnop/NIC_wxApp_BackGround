package group.controller;

import com.alibaba.fastjson.JSONObject;

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

    private void takeMission(HttpServletRequest req, HttpServletResponse resp) {

    }

    private void quitMission(HttpServletRequest req, HttpServletResponse resp) {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
