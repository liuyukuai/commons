/*
 *  
 */
package com.cutefool.commons.ci.maven.executing;

import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.Executor;
import com.cutefool.commons.core.page.Response;

/**
 * maven排序
 *
 * @author 271007729@qq.com
 * @date 2022/9/14 2:35 PM
 */
public class MavenExecutor<T extends CiContext<T>> implements Executor<T> {

    @Override
    public Response<Boolean> sort(T context) {
        return MavenExecutors.sort(context);
    }
}
