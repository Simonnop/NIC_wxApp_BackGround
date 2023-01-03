package group.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import group.controller.util.LogPrinter;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.pojo.Mission;
import group.service.ManagerService;
import group.service.impl.ManagerServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;

@WebServlet("/manage")
public class ManageMissionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ManageMissionServlet.class);

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
                case "getFilePath":
                    getMissionFilePathResponse(req, resp);
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
                logger.error(URLDecoder.decode(req.getQueryString(),"utf-8"));
                LogPrinter.printException(logger,e);
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
        JSONObject dataJson = JSONObject.parseObject(data);
        int missionElement = (int) dataJson.get("element");

        Mission mission = JSONObject.parseObject(data, Mission.class);
        mission.setElement(missionElement);

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

    protected void getMissionFilePathResponse(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
