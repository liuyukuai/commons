/*
 *
 */
package com.cutefool.commons.core;

/**
 * @author 271007729@qq.com
 * @date 9/26/21 4:02 PM
 */
public interface Writeable<T> {
    /**
     * write
     *
     * @param t t
     * @return true or false
     */
    boolean write(T t);
}
