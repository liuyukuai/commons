/*
 *
 */
package com.cutefool.commons.netty.client.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.netty.libs.Processor;

/**
 * @author 271007729@qq.com
 * @date 9/14/21 5:34 PM
 */
public class SocketHandler extends SimpleChannelInboundHandler<String> {

    private Processor<String, Response<?>> processor;

    public SocketHandler(Processor<String, Response<?>> processor) {
        this.processor = processor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        this.processor.onMessage(ctx, msg);
    }
}
