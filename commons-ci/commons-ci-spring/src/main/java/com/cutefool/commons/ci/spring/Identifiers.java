/*
 *  
 */
package com.cutefool.commons.ci.spring;

import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.Identifier;
import com.cutefool.commons.ci.core.Identifying;
import com.cutefool.commons.spring.SpiSpringContext;

/**
 * 部署工具类
 *
 * @author 271007729@qq.com
 * @date 7/11/21 6:04 PM
 */
@SuppressWarnings("unused")
public class Identifiers {

    public static Identifier identifier(CiContext context) {
        Identifying identifying = SpiSpringContext.getSpi(PrefixConst.PREFIX_IDENTIFIER + context.getType(), Identifying.class);
        return identifying.identifier(context);
    }
}
