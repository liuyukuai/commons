/*
 *
 */
package com.cutefool.commons.ci.core;

import com.cutefool.commons.core.page.Response;

/**
 * 可执行的
 *
 * @author 271007729@qq.com
 * @date 7/11/21 4:45 PM
 */
@SuppressWarnings("unused")
public interface Executing<C extends CiContext<C>, R> {

    /**
     * 执行部署
     *
     * @param context context
     * @return R
     */
    Response<R> execute(C context);
}
