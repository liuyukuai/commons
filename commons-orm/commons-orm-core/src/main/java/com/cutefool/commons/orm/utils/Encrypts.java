package com.cutefool.commons.orm.utils;



import java.util.Base64;

/**
 * 加密工具类
 */
@SuppressWarnings("unused")
public class Encrypts {

    public static String encode(String database) {
        return Base64.getEncoder().encodeToString(database.getBytes());
    }

    public static String decode(String database) {
        return new String(Base64.getDecoder().decode(database));
    }
}
