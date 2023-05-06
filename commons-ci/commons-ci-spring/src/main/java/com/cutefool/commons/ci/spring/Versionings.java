/*
 *
 */
package com.cutefool.commons.ci.spring;

import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.Dependency;
import com.cutefool.commons.ci.core.Versioning;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.spring.SpiSpringContext;
import com.cutefool.commons.version.Version;

import java.util.List;

/**
 * 部署工具类
 *
 * @author 271007729@qq.com
 * @date 7/11/21 6:04 PM
 */
@SuppressWarnings("unused")
public class Versionings {

    public static Response<Boolean> update(CiContext context, List<Dependency> dependencies) {
        if (Lists.isEmpty(dependencies)){
            return Response.ok();
        }
        Versioning versioning = SpiSpringContext.getSpi(PrefixConst.PREFIX_VERSIONING + context.getType(), Versioning.class);
        return versioning.update(context, dependencies);
    }

    public static Version version(CiContext context) {
        Versioning versioning = SpiSpringContext.getSpi(PrefixConst.PREFIX_VERSIONING + context.getType(), Versioning.class);
        return versioning.version(context);
    }
}
