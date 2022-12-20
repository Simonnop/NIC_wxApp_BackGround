package group.controller;

import com.alibaba.fastjson.JSONObject;
import group.controller.exception.AppRuntimeException;
import group.controller.exception.ExceptionKind;
import group.controller.util.JsonSwitcher;
import group.pojo.Mission;
import group.pojo.util.MyTime;
import group.service.ManagerService;
import group.service.impl.ManagerServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

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
                result.put("msg", "后端ManageMissionServlet处理错误");
            }

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    protected void addMissionResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        String data = req.getParameter("data");
        /*
        * TODO 改用 parseObject 读取对象
        * */
        Mission mission = JSONObject.parseObject(data, Mission.class);

        ManagerService managerService = new ManagerServiceImpl();
        managerService.addMission(mission);

        result.put("code", 202);
        result.put("msg", "任务添加成功");

        String resultStr = result.toJSONString();
        out.write(resultStr);
        out.flush();
        System.out.println(resultStr);

    }

    protected void alterMissionResponse(HttpServletRequest req, HttpServletResponse resp) {

    }

    protected void deleteMissionResponse(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
