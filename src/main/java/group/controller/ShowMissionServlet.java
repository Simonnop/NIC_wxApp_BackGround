package group.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.controller.util.LogPrinter;
import group.service.ManagerService;
import group.service.impl.ManagerServiceImpl;
import group.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;

@WebServlet("/show")
public class ShowMissionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ShowMissionServlet.class);

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
                case "showGotDraft":
                    showMissionGotDraft(req, resp);
                    break;
                case "showByInput":
                    showMissionByInput(req, resp);
                    // 时间
                    // 状态
                    // 评分
                    // 标签
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
                result.put("msg", "后端ShowMissionServlet处理错误");
                logger.error(URLDecoder.decode(req.getQueryString(),"utf-8"));
                LogPrinter.printException(logger,e);
            }

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    private void showMissionGotDraft(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        ManagerService managerService = new ManagerServiceImpl();

        result.put("data", managerService.showMissionGotDraft());

        result.put("code", 302);
        result.put("msg", "查询已有稿件任务成功");

        String resultStr = result.toJSONString();
        out.write(resultStr);
        out.flush();
        System.out.println(resultStr);
    }

    protected void showAllMission(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        UserServiceImpl userService = new UserServiceImpl();

        result.put("data", userService.showAllMission());

        result.put("code", 302);
        result.put("msg", "全部查询任务成功");

        String resultStr = result.toJSONString();
        out.write(resultStr);
        out.flush();
        System.out.println(resultStr);

    }

    protected void showNeedMission(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        UserServiceImpl userService = new UserServiceImpl();

        result.put("data", userService.showNeedMission());

        result.put("code", 302);
        result.put("msg", "缺人查询任务成功");

        String resultStr = result.toJSONString();
        out.write(resultStr);
        out.flush();
        System.out.println(resultStr);

    }

    protected void showMissionByInput(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        String data = req.getParameter("data");
        JSONObject dataJson = JSONObject.parseObject(data);
        String missionID = (String) dataJson.get("missionID");

        UserServiceImpl userService = new UserServiceImpl();

        if (missionID != null) {
            result.put("data", userService.showMissionById(missionID));
        } else {
            throw new AppRuntimeException(ExceptionKind.REQUEST_INFO_ERROR);
        }

        result.put("code", 302);
        result.put("msg", "指定查询任务成功");

        String resultStr = result.toJSONString();
        out.write(resultStr);
        out.flush();
        System.out.println(resultStr);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
