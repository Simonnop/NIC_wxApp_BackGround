package group.controller;

import com.alibaba.fastjson.JSONObject;
import group.controller.util.LogPrinter;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;
import group.service.impl.UserServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.List;

public class UploadFileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(UploadFileServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 中文处理
        upload.setHeaderEncoding("UTF-8");
        // 这个路径相对当前应用的目录
        String uploadPath = "C:\\ProgramData\\NIC\\work_files";

        Writer out = resp.getWriter();
        JSONObject result = new JSONObject();

        try {
            String missionID = req.getParameter("missionID");
            String userid = req.getParameter("userid");
            // 检测是否为多媒体上传
            if (!ServletFileUpload.isMultipartContent(req)|| missionID == null) {
                System.out.println(missionID);
                throw new AppRuntimeException(ExceptionKind.REQUEST_INFO_ERROR);
            }

            // 解析请求的内容提取文件数据
            List<FileItem> formItems = upload.parseRequest(req);
            // 交给 service 层处理
            UserServiceImpl userService = new UserServiceImpl();
            userService.uploadFile(formItems, missionID, uploadPath);

            result.put("code", 502);
            result.put("msg", "文件上传成功");

        } catch (Exception e) {
            if (e instanceof AppRuntimeException) {
                result.put("code", ((AppRuntimeException) e).getCode());
                result.put("msg", ((AppRuntimeException) e).getMsg());
            } else {
                result.put("code", 98);
                result.put("msg", "后端UploadFileServlet处理错误");
                logger.error(URLDecoder.decode(req.getQueryString(), "utf-8"));
                LogPrinter.printException(logger, e);
            }

        } finally {
            String resultStr = result.toJSONString();
            out.write(resultStr);
            out.flush();
            System.out.println(resultStr);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
