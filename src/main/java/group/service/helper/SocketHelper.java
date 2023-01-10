package group.service.helper;

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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
