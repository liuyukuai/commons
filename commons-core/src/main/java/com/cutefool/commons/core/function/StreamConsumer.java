/*
 *  
 */
package com.cutefool.commons.core.function;

/**
 * 流消费类
 *
 * @author 271007729@qq.com
 * @date 8/8/21 11:57 PM
 */
public interface StreamConsumer {

    /**
     * 消费类
     *
     * @param s lines
     */
    void consumeLine(String s);
}
