/*
 *
 */
package com.cutefool.commons.netty.server.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.netty.libs.Processor;

/**
 * http处理类
 *
 * @author 271007729@qq.com
 * @date 9/14/21 2:54 PM
 */
@Slf4j
public class SocketHandler extends SimpleChannelInboundHandler<String> {

    private Processor<String, Response<?>> processor;

    public SocketHandler(Processor<String, Response<?>> processor) {
        this.processor = processor;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String content) {
        this.processor.onMessage(ctx, content);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
