package com.qiwenshare.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     *  去除字符串中的空格，回车，换行，缩进
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {

//        String intro = str.replaceAll("\\r\\n", ""); //删除回车换行
//        intro = intro.replaceAll("\\r", ""); //删除回车换行
//        intro = intro.replaceAll("\\n", ""); //删除回车换行
        String intro = str.replaceAll("\\t", ""); //删除回车换行
        intro = intro.replaceAll("> +", ">"); //删除回车换行
        intro = intro.replaceAll(" +<", "<"); //删除回车换行
        return intro;
    }

    public static String formatMdContent(String str){
        String intro = str.replaceAll("#+ ", ""); //删除标题格式
        intro = intro.replaceAll("> ", ""); //删除引用格式
        intro = intro.replaceAll("!\\[(.+)\\]\\((.+)\\)", ""); //删除图片
        intro = intro.replaceAll("\\*\\*", ""); //删除加粗
        intro = intro.replaceAll("\\r\\n", ""); //删除回车换行
        intro = intro.replaceAll("\\r", ""); //删除回车换行
        intro = intro.replaceAll("\\n", ""); //删除回车换行
        intro = intro.replaceAll("\\t", ""); //删除回车换行
        return intro;

    }
}