/*
 *  
 */
package com.cutefool.commons.concurrency;

import java.io.Serializable;

/**
 * 创建支持类
 *
 * @author 271007729@qq.com
 * @date 8/31/21 4:46 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ConcurrencySupport {

    public static <T extends Serializable> Concurrency<T> create() {
        return create(1L);
    }

    public static <T extends Serializable> Concurrency<T> create(long num) {
        return create(num, false);
    }


    public static <T extends Serializable> Concurrency<T> create(long num, boolean isRepeat) {
        return new Concurrency<>(num, isRepeat);
    }
}
