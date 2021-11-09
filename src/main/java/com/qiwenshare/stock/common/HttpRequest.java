//package com.qiwenshare.stock.common;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//
//public class HttpRequest {
//
//    /**
//     * 向指定URL发送GET方法的请求
//     * @param url 发送请求的URL
//     * @param param 请求参数
//     * @return URL 所代表远程资源的响应结果
//     */
//    public static String sendGet(String url, String param) {
//        String result = "";
//        StringBuffer requestResult = new StringBuffer();
//        BufferedReader in = null;
//        if (param != null && !param.isEmpty()) {
//            param = "?" + param;
//        } else {
//            param = "";
//        }
//        try {
//            String urlNameString = url + param;
//            URL realUrl = new URL(urlNameString);
//            // 打开和URL之间的连接
//            URLConnection connection = realUrl.openConnection();
//            // 设置通用的请求属性
//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            connection.setRequestProperty("Cookie", "PHPStat_First_Time_10000011=1480428327337; PHPStat_Cookie_Global_User_Id=_ck16112922052713449617789740328; PHPStat_Return_Time_10000011=1480428327337; PHPStat_Main_Website_10000011=_ck16112922052713449617789740328%7C10000011%7C%7C%7C; VISITED_COMPANY_CODE=%5B%22600064%22%5D; VISITED_STOCK_CODE=%5B%22600064%22%5D; seecookie=%5B600064%5D%3A%u5357%u4EAC%u9AD8%u79D1; _trs_uv=ke6m_532_iw3ksw7h; VISITED_MENU=%5B%228451%22%2C%229055%22%2C%229062%22%2C%229729%22%2C%228528%22%5D");
//            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
//            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
//            connection.setRequestProperty("Host", "query.sse.com.cn");
//            connection.setRequestProperty("Referer", "http://www.sse.com.cn/assortment/stock/list/share/");
//            // 建立实际的连接
//            connection.connect();
//            // 定义 BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream(), "gbk"));
//            String line;
//            while ((line = in.readLine()) != null) {
//                requestResult.append(line);
//            }
//        }
//        // 使用finally块来关闭输入流
//        catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//        return requestResult.toString();
//    }
//
//    /**
//     * 向指定URL发送POST方法的请求
//     * @param url 发送请求的 URL
//     * @param param 请求参数
//     * @return 所代表远程资源的响应结果
//     */
//    public static String sendPost(String url, String param) {
//        OutputStreamWriter out = null;
//        BufferedReader in = null;
//        String result = "";
//        StringBuffer requestResult = new StringBuffer();
//        try {
//            URL realUrl = new URL(url);
//            // 打开和URL之间的连接
//            URLConnection conn = realUrl.openConnection();
//            // 设置通用的请求属性
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            // 发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            //out = new PrintWriter(conn.getOutputStream());
//            out = new OutputStreamWriter(conn
//                    .getOutputStream(), "utf-8");
//            // 发送请求参数
//            out.write(param);
//            // flush输出流的缓冲
//            out.flush();
//            // 定义BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream(), "utf-8"));
//            String line;
//            while ((line = in.readLine()) != null) {
//
//                requestResult.append(line);
//            }
//        } catch (Exception e) {
//            System.out.println("发送 POST 请求出现异常！" + e);
//            e.printStackTrace();
//        }
//        //使用finally块来关闭输出流、输入流
//        finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return requestResult.toString();
//    }
//
//}