import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class RCVServer {
	private static boolean startFlag = false;
	public static void startServer(){
		if(!startFlag){
			new Thread(new Runnable() {
				@Override
				public void run() {
					Socket socket = null;
					try {
						ServerSocket s = new ServerSocket(8000, 3);	// 创建一个监听8000端口的服务器Socket
						while (true) {
							System.out.println("等待连接\n");
							socket = s.accept();
							System.out.println("连接已建立。端口号：" + socket.getPort());
							new MyWebThread(socket).start();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	public static void main(String[] args) {
		RCVServer.startServer();
	}
}

