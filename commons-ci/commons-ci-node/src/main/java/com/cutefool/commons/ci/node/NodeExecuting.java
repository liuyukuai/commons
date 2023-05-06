/*
 *  
 */
package com.cutefool.commons.ci.node;

import com.cutefool.commons.ci.core.CiConsumer;
import com.cutefool.commons.ci.core.CiReleaseContext;
import com.cutefool.commons.ci.core.Executing;
import com.cutefool.commons.spring.SpiSpringContext;

/**
 * Nexus 工具类
 *
 * @author 271007729@qq.com
 * @date 7/27/21 3:42 PM
 */
public interface NodeExecuting<T extends CiReleaseContext<T>, R> extends Executing<T, R> {
    /**
     * context
     *
     * @param context context
     * @return context
     */
    default NodeContext context(T context) {
        CiConsumer<T> ciConsumer = CiConsumer.empty(context.getCiProcessConsumer());
        NodeConfig setting;
        try {
            NodeConfigurable oneSpi = SpiSpringContext.getOneSpi(NodeConfigurable.class);
            setting = oneSpi.config();
            ciConsumer.consumer(context, "node has config  npm = " + setting.getNpm() + " registry = " + setting.getRegistry());
        } catch (Exception e) {
            // default
            setting = NodeConfig.defaultConfig();
            ciConsumer.consumer(context, "node not config, npm used default.");
        }
        return NodeContext.builder()
                          .npm(setting.getNpm())
                          .registry(setting.getRegistry())
                          .consumer((s) -> ciConsumer.consumer(context, s))
                          .build();
    }

}
