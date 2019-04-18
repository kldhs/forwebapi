//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.MalformedURLException;
//import java.net.SocketTimeoutException;
//import java.net.URL;
//import java.net.URLConnection;
//
//public class ADC {
//    /**
//     * 根据指定url，编码获得字符串 官网：www.fhadmin.org
//     *
//     * @param address
//     * @param Charset
//     * @return
//     */
//    public static String getStringByConAndCharset(String address, String Charset) {
//        StringBuffer sb;
//        try {
//            URL url = new URL(address);
//            URLConnection con = url.openConnection();
//            BufferedReader bw = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset));
//            String line;
//            sb = new StringBuffer();
//            while ((line = bw.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            bw.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            sb = new StringBuffer();
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 通过post方式获取页面源码
//     *
//     * @param urlString
//     *            url地址
//     * @param paramContent
//     *            需要post的参数
//     * @param chartSet
//     *            字符编码（默认为ISO-8859-1）
//     * @return
//     * @throws IOException
//     */
//    public static String postUrl(String urlString, String paramContent, String chartSet, String contentType) {
//        long beginTime = System.currentTimeMillis();
//        if (chartSet == null || "".equals(chartSet)) {
//            chartSet = "ISO-8859-1";
//        }
//
//        StringBuffer result = new StringBuffer();
//        BufferedReader in = null;
//
//        try {
//            URL url = new URL((urlString));
//            URLConnection con = url.openConnection();
//
//            // 设置链接超时
//            con.setConnectTimeout(3000);
//            // 设置读取超时
//            con.setReadTimeout(3000);
//            // 设置参数
//            con.setDoOutput(true);
//            if (contentType != null && !"".equals(contentType)) {
//                con.setRequestProperty("Content-type", contentType);
//            }
//
//            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), chartSet);
//            writer.write(paramContent);
//            writer.flush();
//            writer.close();
//            in = new BufferedReader(new InputStreamReader(con.getInputStream(), chartSet));
//            String line = null;
//            while ((line = in.readLine()) != null) {
//                result.append(line + "\r\n");
//            }
//            in.close();
//        } catch (MalformedURLException e) {
//            logger.error("Unable to connect to URL: " + urlString, e);
//        } catch (SocketTimeoutException e) {
//            logger.error("Timeout Exception when connecting to URL: " + urlString, e);
//        } catch (IOException e) {
//            logger.error("IOException when connecting to URL: " + urlString, e);
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (Exception ex) {
//                    logger.error("Exception throws at finally close reader when connecting to URL: " + urlString, ex);
//                }
//            }
//            long endTime = System.currentTimeMillis();
//            logger.info("post用时：" + (endTime - beginTime) + "ms");
//        }
//        return result.toString();
//    }
//}
