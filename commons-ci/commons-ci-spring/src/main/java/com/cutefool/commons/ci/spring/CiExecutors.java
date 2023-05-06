/*
 *  
 */
package com.cutefool.commons.ci.spring;

import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.Executor;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.spring.SpiSpringContext;

/**
 * @author 271007729@qq.com
 * @date 2022/9/14 2:38 PM
 */
public final class CiExecutors {
    private CiExecutors() {
    }

    public static <T extends CiContext<T>> Response<Boolean> sort(T context) {
        Executor<T> executing = SpiSpringContext.getSpi(PrefixConst.PREFIX_EXECUTOR + context.getType(), Executor.class);
        return executing.sort(context);
    }
}
