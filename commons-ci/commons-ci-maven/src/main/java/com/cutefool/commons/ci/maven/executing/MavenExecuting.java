/*
 *
 */
package com.cutefool.commons.ci.maven.executing;

import com.cutefool.commons.ci.maven.context.MavenContext;
import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.Executing;

/**
 * @author 271007729@qq.com
 * @date 7/11/21 5:06 PM
 */
@SuppressWarnings("unused")
public interface MavenExecuting<C extends CiContext<C>, R> extends Executing<C, R> {

    /**
     * context
     *
     * @param context context
     * @return context
     */
    @SuppressWarnings("unchecked")
    default MavenContext context(C context) {
        return MavenContext.init(context);
    }

}
