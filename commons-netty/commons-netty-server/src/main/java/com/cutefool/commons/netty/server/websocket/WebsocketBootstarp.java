/*
 *
 */
package com.cutefool.commons.netty.server.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.netty.libs.Processor;

import java.util.concurrent.TimeUnit;

/**
 * websocket启动类
 *
 * @author 271007729@qq.com
 * @date 9/16/21 11:05 AM
 */
public class WebsocketBootstarp {
    public static void start(Processor<String, Response<?>> processor, int port) {
        //一个主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //一个工作线程组
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                // nio
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) {
                        //心跳检测,参数说明：[长时间未写:长时间未读:长时间未读写:时间单位]~[读写是对连接本生而言，写：未向服务端发送消息，读：未收到服务端的消息]
                        channel.pipeline()
                                .addLast(new IdleStateHandler(0, 0, 30 * 3, TimeUnit.SECONDS));
                        // 编码
                        channel.pipeline().addLast(new HttpServerCodec());
                        // 聚合器
                        channel.pipeline().addLast(new HttpObjectAggregator(65536));
                        channel.pipeline().addLast(new ChunkedWriteHandler());
                        channel.pipeline().addLast(new WebSocketHandler(processor));
                    }
                })
                //设置队列大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            //绑定端口,开始接收进来的连接
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭主线程组
            bossGroup.shutdownGracefully();
            //关闭工作线程组
            workGroup.shutdownGracefully();
        }
    }

}
