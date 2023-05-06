/*
 *
 */
package com.cutefool.commons.ci.nexus;

import com.cutefool.commons.ci.core.CiReleaseContext;
import com.cutefool.commons.core.page.Response;

/**
 * Nexus 工具类
 *
 * @author 271007729@qq.com
 * @date 7/27/21 3:42 PM
 */
public class NexusRemoveExecuting<T extends CiReleaseContext<T>> implements NexusExecuting<T,Boolean> {

    @Override
    @SuppressWarnings("unchecked")
    public Response<Boolean> execute(T ciContext) {
        NexusContext context = this.context(ciContext);
        return Response.ok(Nexus.delete(context));
    }
}
