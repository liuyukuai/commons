/*
 *
 */
package com.cutefool.commons.ci.core;

import com.cutefool.commons.core.page.Response;

/**
 * 执行器
 *
 * @author 271007729@qq.com
 * @date 2022/9/14 2:31 PM
 */
public interface Executor<T extends CiContext<T>> {

    /**
     * 排序
     *
     * @param context context
     * @return res
     */
    Response<Boolean> sort(T context);

}
