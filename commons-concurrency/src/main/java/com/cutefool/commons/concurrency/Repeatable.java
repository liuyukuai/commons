package com.cutefool.commons.concurrency;

public interface Repeatable {

    /**
     * 返回对象的唯一标识
     *
     * @return 唯一标识
     */
    String keys();

    /**
     * 返回对象的值
     *
     * @return 值
     */
    Object values();
}
