package com.cutefool.commons.core.digest;

/**
 * 签名执行器
 */
public interface DigestsInvoker {

    /**
     * 校验字符串是否通过
     *
     * @param s 字符串
     * @return true or false
     */
    boolean validate(String s, long expirationTimes);

    /**
     * 获取源数据
     *
     * @param s 加密字符串
     * @return 源字符串
     */
    String getValue(String s, long expirationTimes);

    /**
     * 加载数据
     *
     * @param s 加密串
     * @return 值
     */
    DigestLibs load(String s);

}
