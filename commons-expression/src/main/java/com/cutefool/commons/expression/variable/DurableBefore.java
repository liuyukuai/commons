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
public interface DurableBefore<T, E> {

    /**
     * before
     *
     * @return t
     */
    T before();

    /**
     * before
     *
     * @param e e
     * @return t
     */
    T before(E e);
}
