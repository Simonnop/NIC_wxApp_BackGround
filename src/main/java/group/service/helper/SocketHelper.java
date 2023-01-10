package group.service.helper;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketHelper {

    public static void main(String[] args) throws InterruptedException {

        String ip = "localhost";        // 设置发送地址和端口号
        int port = 12346;
        Socket socket = null;
        try {
            // 连接服务器
            socket = new Socket(ip, port);

            OutputStream ops = socket.getOutputStream();
            OutputStreamWriter opsw = new OutputStreamWriter(ops);
            BufferedWriter bw = new BufferedWriter(opsw);

            String userid = "U208911390";
            String pwd = "123456";

            String stuInfo =  "{\"userid\":\""+ userid +"\",\"pwd\":\""+ pwd +"\"}";

            bw.write(stuInfo);
            bw.flush();

            // 获取输入流
            InputStream in = socket.getInputStream();   //读取数据

            // 包装输入流，输出流，包装一下可以直接传输字符串，不包装的话直接使用InputStream和OutputStream只能直接传输byte[]类型数据
            BufferedReader inRead = new BufferedReader(new InputStreamReader(in));
//			PrintWriter outWriter = new PrintWriter(out);

            // 接受应答
            String result = inRead.readLine();  // 使用了包装后的输入流方便读取消息
            System.out.println(result);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
