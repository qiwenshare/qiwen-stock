package com.qiwenshare.stock.common;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.qiwenshare.stock.constant.ProxyConstant;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class ProxyHttpRequest {

    public static List<ProxyBean> proxyBeans = new ArrayList<>();

    public ProxyHttpRequest() {
        String proxyRequest = getProxyRequestResult(ProxyConstant.SERVER_PROXY_COUNT);
        JSONObject resultObj = JSONObject.parseObject(proxyRequest);
        String requestJson = JSON.toJSONString(resultObj.getObject("data", List.class));
        proxyBeans = JSON.parseArray(requestJson, ProxyBean.class);
    }

    /**
     * 获取代理列表
     *
     * @return 返回代理列表
     */
    public static String getProxyRequestResult(int count) {
        StringBuffer requestResult = new StringBuffer();
        BufferedReader in = null;

        try {
            URL realUrl = new URL("https://" + ProxyConstant.SERVER_IP + "/api/proxy/getproxylist?count=" + count);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                requestResult.append(line);
            }
        }
        // 使用finally块来关闭输入流
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }


        return requestResult.toString();
    }

    public static void main(String[] args) {
        //初始化对象
        ProxyHttpRequest proxyHttpRequest = new ProxyHttpRequest();
        String url = "http://query.sse.com.cn/security/stock/getStockListData2.do";
        //String param = "isPagination=true&stockCode=&csrcCode=&areaName=&stockType=" + stockType + "&pageHelp.cacheSize=1&pageHelp.beginPage=1&pageHelp.pageSize=3000&pageHelp.pageNo=1&_=1553181823571";
        Map<String, String> param = new HashMap<String, String>();
        param.put("isPagination", "true");
        param.put("stockType", "1");
        param.put("pageHelp.cacheSize", "1");
        param.put("pageHelp.beginPage", "1");
        param.put("pageHelp.pageSize", "3000");
        param.put("pageHelp.pageNo", "1");
        param.put("_", "1584275438648");


        //参数
//        Map<String, String> param = new HashMap<>();
//        param.put("v", "3.1.9y");
        //发送请求
        String result = proxyHttpRequest.sendGet(url, param);
        System.out.println(result);
    }

    public String sendGet(String url, Map<String, String> param) {
        ProxyBean proxyBean = null;
        int currentRandomIndex = 0;
        if (proxyBeans.size() <= 1) {
            String proxyRequest = getProxyRequestResult(ProxyConstant.SERVER_PROXY_COUNT);
            JSONObject resultObj = JSONObject.parseObject(proxyRequest);
            String requestJson = JSON.toJSONString(resultObj.getObject("data", List.class));
            proxyBeans = JSON.parseArray(requestJson, ProxyBean.class);
            if (proxyBeans.size() <= 1) {
                return "没有可用的代理";
            }
            currentRandomIndex = new Random().nextInt(proxyBeans.size());
            proxyBean = (ProxyBean) proxyBeans.get(currentRandomIndex);
        } else {
            currentRandomIndex = new Random().nextInt(proxyBeans.size());
            proxyBean = (ProxyBean) proxyBeans.get(currentRandomIndex);
        }

        boolean isRequestSuccess = false;
        Document doc = null;

        try {
            doc = Jsoup.connect(url)
                    .timeout(60000)
                    .proxy(proxyBean.getProxyip(), proxyBean.getProxyport())
                    .data(param)
                    .ignoreContentType(true)
                    .userAgent(ProxyConstant.userAgentArr[new Random().nextInt(ProxyConstant.userAgentArr.length)])
                    .header("Referer", "http://www.sse.com.cn/assortment/stock/list/share/")//这个来源记得换..
                    .get();
            isRequestSuccess = true;
        } catch (IOException e) {
            System.out.println(e);
            isRequestSuccess = false;
            if (proxyBeans.size() > currentRandomIndex) {
                proxyBeans.remove(currentRandomIndex);
            }

//            ProxyBean proxyBean1 = new ProxyBean();
//            proxyBean1.setProxyip("123.56.9.174");
//            proxyBean1.setProxyport(8761);
//            proxyBeans.add(proxyBean1);
//            System.out.println("此代理不通，正在重试。。" + JSON.toJSONString(proxyBean));
        }
        if (!isRequestSuccess
                || doc.text().indexOf("Welcome To Zscaler Directory Authentication") != -1
                || doc.text().indexOf("ACCESS DENIED") != -1
                || doc.text().indexOf("If you have the access code") != -1
                || doc.text().indexOf("DansGuardian") != -1
                || doc.text().indexOf("Pagina nueva") != -1
                || doc.text().indexOf("Access denied") != -1
                || doc.text().indexOf("Sukanda OneLink") != -1
                || doc.text().indexOf("管理后台") != -1
        || StringUtils.isEmpty(doc.text())) {
            return sendGet(url, param);
        }

        return doc.text();
    }
}