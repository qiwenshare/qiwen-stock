package com.qiwenshare.common.operation;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

public class EncryptOperation {

    public static String encodeByMd5(String string) throws UnsupportedEncodingException {
        return DigestUtils.md5DigestAsHex(string.getBytes("utf-8"));
    }

}