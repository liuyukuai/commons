/*
 *
 */
package com.cutefool.commons.ci.spring;

import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.Executing;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.spring.SpiSpringContext;

/**
 * 部署工具类
 *
 * @author 271007729@qq.com
 * @date 7/11/21 6:04 PM
 */
public class Releases {

    public static Response<Boolean> release(CiContext context) {
        Executing executing = SpiSpringContext.getSpi(PrefixConst.PREFIX_RELEASE + context.getType(), Executing.class);
        return executing.execute(context);
    }
}
