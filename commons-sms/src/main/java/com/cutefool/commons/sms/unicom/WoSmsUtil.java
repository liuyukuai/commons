/*
 *
 */
package com.cutefool.commons.sms.unicom;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 联通 短信MD5
 *
 * @author 271007729@qq.com
 * @date 2020/12/4 19:00
 */
public class WoSmsUtil {

    /**
     * 计算MD5值
     *
     * @param text 文本
     * @return String
     */
    public static String makeMd5(String text) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (byte value : b) {
                i = value;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            md5 = buf.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }


    /**
     * 文档 DEMO
     */
    public static void test() {
        String cpcode = "AAAAAC";
        String msg = "模板短信测试,0001";
        String mobiles = "18607714703,18648807841";
        String excode = "";
        String templetid = "2";
        String key = "f75b9467e1112239";
        String md5source = cpcode + msg + mobiles + excode + templetid + key;
        String md5str = WoSmsUtil.makeMd5(md5source);
        System.out.println(md5str);
    }


}

