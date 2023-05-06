/*
 *
 */
package com.cutefool.commons.core;

import java.util.Objects;

/**
 * 状态工具类
 *
 * @author 271007729@qq.com
 * @date 2019/10/25 4:23 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class Status {

    /***
     *  删除标识
     */
    public static final Byte STATUS_FALSE = 0;

    /***
     * 删除标识
     */
    public static final Byte STATUS_TRUE = 1;

    /**
     * false
     */
    public final static String FALSE = "0";

    /**
     * true
     */
    public final static String TRUE = "1";

    /**
     * 判断状态是true还是false
     *
     * @param status status
     * @return true |false
     */
    public static boolean isTrue(String status) {
        return Objects.equals(TRUE, status);
    }

    /**
     * 判断状态是true还是false
     *
     * @param status status
     * @return true |false
     */
    public static boolean isTrue(Byte status) {
        return Objects.equals(TRUE, String.valueOf(status));
    }

    /**
     * 判断状态是true还是false
     *
     * @param status status
     * @return true |false
     */
    public static Byte get(Boolean status) {
        return Objects.nonNull(status) && status ? STATUS_TRUE : STATUS_FALSE;
    }

}
