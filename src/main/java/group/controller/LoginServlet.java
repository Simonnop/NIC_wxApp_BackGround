package group.controller;

import com.alibaba.fastjson.JSONObject;
import group.pojo.User;
import group.service.UserService;
import group.service.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
            }
        } catch (Exception e) {
            Writer out = resp.getWriter();
            JSONObject result = new JSONObject();

            result.put("code", 103);
            result.put("msg", "登录信息错误");

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    protected void signUpResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        try {
            String data = req.getParameter("data");
            JSONObject dataJson = JSONObject.parseObject(data);

            String username = (String) dataJson.get("username");
            String password = (String) dataJson.get("password");

            UserService loginService = new UserServiceImpl();
            User user = loginService.tryLogin(username);

            if (user == null) {
                result.put("code", 100);
                result.put("msg", "查无此用户");
            } else if (!Objects.equals(user.getPassword(), password)) {
                result.put("code", 101);
                result.put("msg", "密码错误");
            } else {
                result.put("code", 102);
                result.put("msg", "登录成功");
            }
        } catch (Exception e) {
            result.put("code", 103);
            result.put("msg", "登录信息错误");
        } finally {

            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    protected void signInResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        try {
            String data = req.getParameter("data");
            JSONObject dataJson = JSONObject.parseObject(data);

            String username = (String) dataJson.get("username");

            UserService loginService = new UserServiceImpl();
            User user = loginService.tryLogin(username);

            if (user == null) {
                result.put("code", 100);
                result.put("msg", "查无此用户");
            } else {
                result.put("code", 102);
                result.put("msg", "登录成功");
            }
        } catch (Exception e) {
            result.put("code", 103);
            result.put("msg", "登录信息错误");
        } finally {
            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }

    }

    protected void touristResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        try {
            result.put("code", 102);
            result.put("msg", "登录成功");
        } catch (Exception e) {
            result.put("code", 103);
            result.put("msg", "登录信息错误");
        } finally {
            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
