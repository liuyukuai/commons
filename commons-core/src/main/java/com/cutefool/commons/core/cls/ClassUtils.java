/*
 *
 */
package com.cutefool.commons.core.cls;

import java.util.Objects;

/**
 * class工具类
 *
 * @author 271007729@qq.com
 * @date 2019/9/25 10:49 AM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ClassUtils {


    /**
     * 判断是否为java内置的类
     *
     * @param clz class
     * @return true:是，false：否
     */
    public static boolean isJavaClass(Class<?> clz) {
        return Objects.nonNull(clz) && clz.getClassLoader() == null;
    }


    /**
     * 判断是否为java内置的类
     *
     * @param clz class
     * @return true:是，false：否
     */
    public static boolean isJavaClass(String clz) {
        try {
            Class<?> aClass = Class.forName(clz);
            return isJavaClass(aClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    /**
     * 判断是否为java内置的类
     *
     * @param object object
     * @return true:是，false：否
     */
    public static boolean isJavaClass(Object object) {
        return Objects.nonNull(object) && isJavaClass(object.getClass());
    }
}
