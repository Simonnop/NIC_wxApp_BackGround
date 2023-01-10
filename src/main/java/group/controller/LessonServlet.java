package group.controller;

import com.alibaba.fastjson.JSONObject;
import group.controller.util.LogPrinter;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.service.UserService;
import group.service.helper.SocketHelper;
import group.service.helper.UserHelper;
import group.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.ArrayList;

public class LessonServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String method = req.getParameter("method");
            switch (method) {
                case "add":
                    addLessonResponse(req, resp);
                    break;
                case "get":
                    getLessonResponse(req, resp);
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
                result.put("msg", "后端LessonServlet处理错误");
                logger.error(URLDecoder.decode(req.getQueryString(), "utf-8"));
                LogPrinter.printException(logger, e);
            }

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    private void getLessonResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        String data = req.getParameter("data");
        JSONObject dataJson = JSONObject.parseObject(data);

        String userid = (String) dataJson.get("userid");
        Integer weekStart = (Integer) dataJson.get("weekStart");
        Integer weekEnd = (Integer) dataJson.get("weekEnd");
        if (userid == null) {
            throw new AppRuntimeException(ExceptionKind.REQUEST_INFO_ERROR);
        }
        UserService userService = new UserServiceImpl();

        ArrayList<Document> lessons = userService.showLessons(userid, weekStart, weekEnd);

        result.put("code", 702);
        result.put("msg", "课表查询成功");
        result.put("data", lessons);

        String resultStr = result.toJSONString();
        out.write(resultStr);
        out.flush();
        System.out.println(resultStr);
    }

    protected void addLessonResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        String data = req.getParameter("data");
        JSONObject dataJson = JSONObject.parseObject(data);

        String userid = (String) dataJson.get("userid");
        String password = (String) dataJson.get("password");
        if (userid == null && password == null) {
            throw new AppRuntimeException(ExceptionKind.REQUEST_INFO_ERROR);
        }

        String lesson = null;
        for (; ; ) {
            if (lesson == null) {
                lesson = SocketHelper.getSocketHelper().getUserLesson(userid, password);
            } else {
                break;
            }
        }

        result.put("code", 102);
        result.put("msg", "课表爬取成功");
        // result.put("data", lesson);

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
