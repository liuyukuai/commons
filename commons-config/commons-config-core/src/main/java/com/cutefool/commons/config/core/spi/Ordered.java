/*
 *  
 */
package com.cutefool.commons.config.core.spi;

/**
 * @author 271007729@qq.com
 * @date 2022/7/26 12:11 AM
 */
@SuppressWarnings("unused")
public interface Ordered {

    /**
     * min order
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * max order
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * get order
     *
     * @return order
     */
    int getOrder();

}
