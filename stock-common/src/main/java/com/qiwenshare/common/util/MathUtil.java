package com.qiwenshare.common.util;

import java.util.Random;

public class MathUtil {

    public static String randomCode(int num) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
