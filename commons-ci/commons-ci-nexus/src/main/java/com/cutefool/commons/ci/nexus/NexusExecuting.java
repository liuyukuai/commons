/*
 *
 */
package com.cutefool.commons.ci.nexus;

import com.cutefool.commons.ci.core.CiReleaseContext;
import com.cutefool.commons.ci.core.Executing;

/**
 * Nexus 工具类
 *
 * @author 271007729@qq.com
 * @date 7/27/21 3:42 PM
 */
public interface NexusExecuting<T extends CiReleaseContext<T>, R> extends Executing<T, R> {
    /**
     * context
     *
     * @param context context
     * @return context
     */
    default NexusContext context(T context) {
        return NexusContext.init(context);
    }

}
