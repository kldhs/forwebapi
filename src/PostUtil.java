import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class PostUtil {
	public static void main(String[] args) {
		String taskNo=getTaskId();
		int carNo=74;
		int point = 20161008;
		move(carNo, taskNo, point);
	}
	private static String getTaskId(){
		return "suray-"+System.currentTimeMillis();
	}
	private static void changeLayer(int carNo,String taskNo,int fromPoint,int toPoint){
		boolean occupancy = occupancy(carNo, taskNo);
		if(occupancy){
			System.err.println("预占成功。。。");
			changeLayerTask(carNo, taskNo, fromPoint, toPoint);
		}else{
			System.err.println("预占失败 。。。");
		}
	}
	
	private static void move(int carNo,String taskNo,int point){
		boolean moveOccupancy = moveOccupancy(carNo, taskNo);
		if(moveOccupancy){
			System.err.println("预占成功。。。");
			moveHandler(carNo, taskNo, point);
		}else{
			System.err.println("预占失败 。。。");
		}
	}
	
	private static boolean moveOccupancy(int carNo,String taskNo){	
		String url = "http://127.0.0.1:8000/fourwayagv/api/agvManage/occupancyCar";
		String param = "{\"taskNo\": \""+taskNo+"\",\"carName\": \""+carNo+"\"}";
		System.out.println(param);
		String result = sendPost(url, param);
		if(result!=null&&result.contains("true")){
			return true;
		}else{
			return false;
		}
	}
	
	private static void moveHandler(int carNo,String taskNo,int point){
		String url = "http://127.0.0.1:8000/fourwayagv/api/task/receive";
		String param = "{\"uuid\":\""+taskNo+"\",\"taskNo\":\""+taskNo+"\",\"taskType\":\"31\",\"taskFlag\":\"null\",\"businessType\":\"31\",\"arriveType\":\"null\",\"carNo\":"+carNo+",\"oneP\":\"null\",\"twoP\":\""+point+"\",\"threeP\":\"null\",\"targetDirection\":\"null\",\"weight\":\"0\",\"containerNo\":\"null\",\"turnPonitParam\":\"null\",\"leaveWorkStationPoint\":\"null\",\"chargingPileId\":\"null\",\"parkingId\":\"null\",\"mapAreaId\":\"801\",\"orgNo\":\"724\",\"distributeNo\":\"601\",\"warehouseNo\":\"2\",\"waitPoint\":\"null\",\"priority\":null, \"offlineReason\":null}";
		System.out.println(param);
		sendPost(url, param);
	}
	
	private static void changeLayerTask(int carNo,String taskNo,int fromPoint,int toPoint){
		int fromLayerNo = getLayerByLocation(fromPoint);
		int toLayerNo = getLayerByLocation(toPoint);
		String url = "http://10.43.160.70:80/fourwayagv/api/task/changeLayerTask";
		String param = "{\"uuid\":\"waps-333-666\", \"carNo\":"+carNo+", \"taskNo\":\""+taskNo+"\", \"fromPoint\":\""+fromPoint+"\", \"toPoint\":\""+toPoint+"\", \"fromLayerNo\":\""+fromLayerNo+"\", \"toLayerNo\":\""+toLayerNo+"\", \"deviceNo\":\"2\", \"areaId\":\"801\", \"warehouseNo\":\"2\", \"distributeNo\":\"601\", \"orgNo\":\"724\"}";
		System.out.println(param);
		sendPost(url, param);
	}
	
	
	private static int getLayerByLocation(int location) {
		// TODO Auto-generated method stub
		if(location>10000000&&10000000<40000000){
		return location/10000000;
		}else{
			throw new IllegalArgumentException("location is Illegal:"+location);
		}
	}

	private static boolean occupancy(int carNo,String taskNo){
		String url = "http://10.43.160.70:80/fourwayagv/api/car/occupancy";
		String param = "{\"taskNo\": \""+taskNo+"\",\"carNo\": \""+carNo+"\"}";
		String result = sendPost(url, param);
		System.out.println(result);
		if(result!=null&&result.contains("true")){
			return true;
		}else{
			return false;
		}
//		System.out.println(param);
	}
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url 发送请求的 URL
	 * @param param 请求参数，//请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestProperty("User-Agent", "Jakarta Commons-HttpClient/3.1");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			//1.获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			//2.中文有乱码的需要将PrintWriter改为如下
			//out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8")
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		System.out.println("post推送结果："+result);
		return result;
	}
}
