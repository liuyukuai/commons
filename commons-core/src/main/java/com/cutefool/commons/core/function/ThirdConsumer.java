package com.cutefool.commons.core.function;

import java.util.Objects;

/**
 * @author 271007729@qq.com
 * @date 2019-07-03 09:40
 */

@FunctionalInterface
@SuppressWarnings("unused")
public interface ThirdConsumer<T, D, E> {
    /**
     * 函数执行方法
     *
     * @param t first参数
     * @param u second参数
     * @param e third参数
     */
    void accept(T t, D u, E e);


    /**
     * Returns a composed {@code BiConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code BiConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default ThirdConsumer<T, D, E> andThen(ThirdConsumer<? super T, ? super D, ? super E> after) {
        Objects.requireNonNull(after);

        return (t, d, e) -> {
            accept(t, d, e);
            after.accept(t, d, e);
        };
    }

}
