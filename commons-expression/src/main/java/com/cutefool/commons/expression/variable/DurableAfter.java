/*
 *
 */
package com.cutefool.commons.expression.variable;

/**
 * 可持续的
 *
 * @author 271007729@qq.com
 * @date 9/12/21 5:25 PM
 */
public interface DurableAfter<T, E> {

    /**
     * after
     *
     * @return t
     */
    T after();

    /**
     * after
     *
     * @param e e
     * @return t
     */
    T after(E e);
}
