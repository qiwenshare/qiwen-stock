package com.qiwenshare.common.proxy;

import org.jsoup.Jsoup;

import java.io.IOException;

public class ProxyUtil {


    public static boolean connectTest(String ip, int port) {
        boolean isTestSuccess = true;
        try {
            Jsoup.connect("http://www.ityouknow.com/")
                    .timeout(5000)
                    .proxy(ip, port)
                    .get();
        } catch (IOException e) {
            System.out.println(e);
            isTestSuccess = false;
        }
        return isTestSuccess;
    }

    public static void main(String[] args) {
        boolean result = ProxyUtil.connectTest("123.56.9.174", 8761);
        System.out.println(result);

    }
}
