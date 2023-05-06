package com.cutefool.commons.core.function;

/**
 * @author 271007729@qq.com
 * @date 2019-07-03 09:40
 */
@FunctionalInterface
public interface ThirdFunction<R, F, S, T> {
    /**
     * 函数执行方法
     *
     * @param f first参数
     * @param s second参数
     * @param t third参数
     * @return 返回值
     */
    R apply(F f, S s, T t);

}
