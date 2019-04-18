import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

class MyWebThread extends Thread {
    private Socket socket;

    MyWebThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStreamReader is = new InputStreamReader(socket.getInputStream());
            char[] bs = new char[2048];
            PrintStream out;
            out = new PrintStream(socket.getOutputStream());
            StringBuilder msg = new StringBuilder();
//			socket.setSoTimeout(10);// 如果10毫秒还没有数据，则视同没有新的数据了。因为有Keep-Alive的缘故，浏览器可能不主动断开连接的。
            socket.setSoTimeout(100); // 实际应用，会根据协议第一行是GET还是 POST确定。
            int len = -1;
            try {
                while ((len = is.read(bs)) != -1) {
                    msg.append(bs, 0, len);
                    msg.append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String msg_str = msg.toString();
            System.out.println("接收到："+msg_str);
            String result = handler(msg_str);
            out.println("HTTP/1.1 200 OK"); // 1、输出响应头信息
            out.println("Content-Type:text/html;charset:UTF-8");
            out.println();
            out.println(result);// 2、输出主页信息
            out.flush();
            out.close();
            is.close();
            socket.close(); // 关闭连接
            System.out.println("连接关闭");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String handler(String msg_str) {
        if (msg_str == null || msg_str.length() == 0) {
            return "";
        }
        String url = getUrl(msg_str);
        String jsonMsg = getJsonMsg(msg_str);
        String result = "{\"code\": \"0\",\"message\": \"成功\",\"reqCode\": \"1541954B96B1112\"}";
        if (result == null || result.trim().length() == 0) {
            return "";
        }
        return result;
    }


    private static String getUrl(String msg_str) {
        String begin = "POST";
        String end = "HTTP/";
        int index_start = msg_str.indexOf(begin) + begin.length();
        int index_end = msg_str.indexOf(end);
        if (index_start >= 0) {
            String temp = msg_str.substring(index_start, index_end);
            temp = temp.trim();
            System.out.println("接收到:" + temp);
            return temp;
        } else {
            System.out.println(msg_str);
        }
        return null;
    }

    private static String getJsonMsg(String msg_str) {
        int index = msg_str.indexOf("\n\r\n");
        if (index > 0) {
            String temp = msg_str.substring(index);
            temp = temp.trim();
            System.out.println("接收到:" + temp);
            return temp;
        } else {
            System.out.println(msg_str);
        }
        return null;
    }
}