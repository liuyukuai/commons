/*
 *
 */
package com.cutefool.commons.netty.libs;

import io.netty.channel.ChannelHandlerContext;
import com.cutefool.commons.core.util.JsonUtils;

/**
 * 处理类
 *
 * @author 271007729@qq.com
 * @date 9/14/21 6:03 PM
 */
@FunctionalInterface
public interface Processor<M, R> {

    /**
     * 处理
     *
     * @param context context
     * @param m       message
     * @return res
     */
    default void onMessage(ChannelHandlerContext context, M m) {
        R r = this.doMessage(m);
        this.writeResponse(context, r);
    }

    /**
     * 处理
     *
     * @param m message
     * @return res
     */
    R doMessage(M m);

    /**
     * 响应信息
     *
     * @param context context
     * @param res     res
     */
    default void writeResponse(ChannelHandlerContext context, R res) {
        context.writeAndFlush(JsonUtils.toJson(res));
    }

}
