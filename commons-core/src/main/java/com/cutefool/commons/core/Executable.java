/*
 *
 */
package com.cutefool.commons.core;

/**
 * 可执行接口
 *
 * @author 271007729@qq.com
 * @date 5/31/21 2:11 PM
 */
@SuppressWarnings("unused")
public interface Executable<T> {

    /**
     * 可执行接口
     *
     * @return true or false
     */
    T execute();
}
