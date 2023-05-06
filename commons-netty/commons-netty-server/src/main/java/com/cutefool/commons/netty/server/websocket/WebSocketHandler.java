/*
 *
 */
package com.cutefool.commons.netty.server.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.netty.libs.Processor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 271007729@qq.com
 * @date 9/16/21 11:07 AM
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");

    private Processor<String, Response<?>> processor;

    public WebSocketHandler(Processor<String, Response<?>> processor) {
        this.processor = processor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame text) {
        // 获取客户端发送过来的文本消息
        for (Channel client : clients) {
            // 将消息发送到所有的客户端
            client.writeAndFlush(new TextWebSocketFrame(sdf.format(new Date()) + ":" + text));
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        clients.remove(ctx.channel());
    }
}
