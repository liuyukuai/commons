/*
 *
 */
package com.cutefool.commons.expression.variable;

import com.cutefool.commons.core.DurationLibs;

import java.time.LocalDateTime;

/**
 * 可持续的
 *
 * @author 271007729@qq.com
 * @date 9/12/21 5:25 PM
 */
public interface Durable<T, E> extends DurableAfter<T, E>, DurableBefore<T, E> {

    /**
     * 时间区间
     *
     * @return 区间
     */
    DurationLibs<LocalDateTime, String> duration();
}
