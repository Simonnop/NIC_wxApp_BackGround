package group.controller;

import com.alibaba.fastjson.JSONObject;
import group.pojo.User;
import group.service.LoginService;
import group.service.LoginServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        LoginService loginService = new LoginServiceImpl();
        User user = loginService.tryLogin(username);

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

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
