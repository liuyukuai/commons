/*
 *
 */
package com.cutefool.commons.ci.spring;

import com.cutefool.commons.ci.core.CiDeployContext;
import com.cutefool.commons.ci.core.Executing;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.spring.SpiSpringContext;

/**
 * 部署工具类
 *
 * @author 271007729@qq.com
 * @date 7/11/21 6:04 PM
 */
public class Deploys {

    public static <T extends CiDeployContext<T>> Response<Boolean> deploy(T context) {
        Executing<T, Boolean> executing = SpiSpringContext.getSpi(PrefixConst.PREFIX_DEPLOY + context.getType(), Executing.class);
        return executing.execute(context);
    }
}
