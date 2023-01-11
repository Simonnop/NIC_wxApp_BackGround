package group.service.helper;

import group.dao.LessonDao;
import group.dao.impl.LessonDaoImpl;
import group.exception.AppRuntimeException;
import group.exception.ExceptionKind;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class SocketHelper {

    private static final SocketHelper socketHelper = new SocketHelper();
    private SocketHelper() {
    }
    public static SocketHelper getSocketHelper() {
        return socketHelper;
    }

    final LessonDaoImpl lessonDao = LessonDaoImpl.getLessonDao();

    public String getUserLesson(String userid, String password) {
        String ip = "localhost";        // 设置发送地址和端口号
        int port = 12346;
        Socket socket = null;
        String result = null;
        try {
            // 连接服务器
            socket = new Socket(ip, port);

            OutputStream ops = socket.getOutputStream();
            OutputStreamWriter opsw = new OutputStreamWriter(ops);
            BufferedWriter bw = new BufferedWriter(opsw);

            String stuInfo = "{\"userid\":\"" + userid + "\",\"pwd\":\"" + password + "\"}";

            bw.write(stuInfo);
            bw.flush();

            // 获取输入流
            InputStream in = socket.getInputStream();   //读取数据

            // 包装输入流，输出流，包装一下可以直接传输字符串，不包装的话直接使用InputStream和OutputStream只能直接传输byte[]类型数据
            BufferedReader inRead = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
//			PrintWriter outWriter = new PrintWriter(out);

            // 接受应答
            result = inRead.readLine();
            System.out.println(result);

            if (result.equals("[{'week': 1, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 2, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 3, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 4, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 5, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 6, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 7, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 8, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 9, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 10, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 11, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 12, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 13, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 14, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 15, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 16, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 17, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 18, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 19, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}, {'week': 20, 'time': [{'weekday': 1, 'lesson': []}, {'weekday': 2, 'lesson': []}, {'weekday': 3, 'lesson': []}, {'weekday': 4, 'lesson': []}, {'weekday': 5, 'lesson': []}, {'weekday': 6, 'lesson': []}, {'weekday': 7, 'lesson': []}]}]")) {
                result = null;
            }

        } catch (IOException e) {
            throw new AppRuntimeException(ExceptionKind.SOCKET_CONNECTION_ERROR);
        }

        if (!(result == null)){
            lessonDao.addLessonInfo(userid, result);
        }

        return result;
    }
}
