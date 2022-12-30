package group.controller;

import com.alibaba.fastjson.JSONObject;
import group.controller.util.LogPrinter;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.pojo.User;
import group.service.UserService;
import group.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.Objects;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String method = req.getParameter("method");
            switch (method) {
                case "signUp":
                    signUpResponse(req, resp);
                    break;
                case "signIn":
                    signInResponse(req, resp);
                    break;
                case "tourist":
                    touristResponse(req, resp);
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
                result.put("msg", "后端LoginServlet处理错误");
                logger.error(URLDecoder.decode(req.getQueryString(), "utf-8"));
                LogPrinter.printException(logger, e);
            }

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    protected void signUpResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        String data = req.getParameter("data");
        JSONObject dataJson = JSONObject.parseObject(data);

        String username = (String) dataJson.get("username");
        String password = (String) dataJson.get("password");
        if (username == null && password == null) {
            throw new AppRuntimeException(ExceptionKind.REQUEST_INFO_ERROR);
        }

        UserService userService = new UserServiceImpl();
        Boolean correctLogin = userService.checkUser(username, password);

        if (correctLogin) {
            result.put("code", 102);
            result.put("msg", "登录成功");
            result.put("data", userService.getUserInfo(username));
        } else {
            result.put("code", 101);
            result.put("msg", "密码错误");
        }

        String resultStr = result.toJSONString();
        out.write(resultStr);
        out.flush();
        System.out.println(resultStr);

    }

    protected void signInResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        String data = req.getParameter("data");
        JSONObject dataJson = JSONObject.parseObject(data);

        String username = (String) dataJson.get("username");

        result.put("code", 102);
        result.put("msg", "登录成功");

        String resultStr = result.toJSONString();
        out.write(resultStr);
        out.flush();
        System.out.println(resultStr);
    }

    protected void touristResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        result.put("code", 102);
        result.put("msg", "登录成功");

        String resultStr = result.toJSONString();
        out.write(resultStr);
        out.flush();
        System.out.println(resultStr);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
