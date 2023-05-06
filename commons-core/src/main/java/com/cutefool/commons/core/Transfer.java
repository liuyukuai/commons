package com.cutefool.commons.core;

/**
 * @author 271007729@qq.com
 * @date 2019-07-03 09:29
 */
public interface Transfer<T, R> {
    /**
     * 转换
     *
     * @param t 原类型
     * @return r 目标类型
     */
    R apply(T t);
}
