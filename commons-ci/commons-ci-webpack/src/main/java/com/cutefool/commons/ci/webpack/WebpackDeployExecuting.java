/*
 *
 */
package com.cutefool.commons.ci.webpack;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.ci.core.CiDeployContext;
import com.cutefool.commons.ci.core.Executing;
import com.cutefool.commons.core.page.Response;

/**
 * @author 271007729@qq.com
 * @date 8/13/21 11:14 AM
 */
@Slf4j
public class WebpackDeployExecuting<T extends CiDeployContext<T>> implements Executing<T, Boolean> {

    @Override
    public Response<Boolean> execute(T context) {
        return Response.ok();
    }
}
