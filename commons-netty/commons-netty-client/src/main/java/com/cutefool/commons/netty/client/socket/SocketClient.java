/*
 *
 */
package com.cutefool.commons.netty.client.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.netty.libs.Processor;

import java.nio.charset.StandardCharsets;

/**
 * @author 271007729@qq.com
 * @date 9/14/21 4:19 PM
 */
@Slf4j
public class SocketClient {

    private String host;

    private int port;

    private Processor<String, Response<?>> processor;

    public SocketClient(String host, int port, Processor<String, Response<?>> processor) {
        this.host = host;
        this.port = port;
        this.processor = processor;
    }

    public Channel start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                        ch.pipeline().addLast(new SocketHandler(processor));
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                    }
                });
        b.option(ChannelOption.SO_KEEPALIVE, true);
        try {
            // 发起连接
            final ChannelFuture future = b.connect(host, port).sync();
            return future.channel();
        } catch (InterruptedException e) {
            throw e;
        }
    }
}
