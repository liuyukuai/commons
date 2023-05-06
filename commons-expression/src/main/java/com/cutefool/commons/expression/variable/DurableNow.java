/*
 *
 */
package com.cutefool.commons.expression.variable;

/**
 * 当前时间
 *
 * @author 271007729@qq.com
 * @date 2022/5/20 2:14 PM
 */
public interface DurableNow<T, E> {

    /**
     * before
     *
     * @return t
     */
    T now();

    /**
     * before
     *
     * @param e e
     * @return t
     */
    T now(E e);
}
