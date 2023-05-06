/*
 *
 */
package com.cutefool.commons.ci.core;

import java.util.Objects;

/**
 * 详细日志记录
 *
 * @author 271007729@qq.com
 * @date 7/11/21 6:22 PM
 */
public interface CiConsumer<T> {

    /**
     * 写入日志
     *
     * @param str     str 日志
     * @param context context 租户ID
     */
    void consumer(T context, String str);

    /**
     * 判空类
     *
     * @param ciConsumer ciConsumer
     * @return ciConsumer
     */
    static <T extends CiContext<T>> CiConsumer<T> empty(CiConsumer<T> ciConsumer) {
        return Objects.nonNull(ciConsumer) ? ciConsumer : (ctx, s) -> {
        };
    }
}
