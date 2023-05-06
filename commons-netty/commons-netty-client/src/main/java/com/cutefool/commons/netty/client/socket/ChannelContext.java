///*
// *
// */
//package com.cutefool.commons.netty.client.socket;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.Channel;
//import lombok.extern.slf4j.Slf4j;
//import com.cutefool.commons.core.page.Response;
//import com.cutefool.commons.netty.libs.Processor;
//
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * Channel Context
// *
// * @author 271007729@qq.com
// * @date 9/14/21 6:00 PM
// */
//@Slf4j
//public class ChannelContext {
//
//    private volatile Bootstrap bootstrap;
//
//    private static volatile ChannelContext instance = null;
//
//    private static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();
//
//    private ChannelContext(Processor<String, Response<?>> processor) {
//        bootstrap = SocketClient.start(processor);
//    }
//
//    public static ChannelContext newInstance(Processor<String, Response<?>> processor) {
//        if (Objects.isNull(instance)) {
//            synchronized (ChannelContext.class) {
//                if (Objects.isNull(instance)) {
//                    instance = new ChannelContext(processor);
//                }
//            }
//        }
//        return instance;
//    }
//
//    public Channel getChannel(String ip, int port) throws InterruptedException {
//        String key = ip + port;
//        Channel channel = CHANNEL_MAP.get(key);
//        if (Objects.nonNull(channel)) {
//            return channel;
//        }
//        channel = this.bootstrap.connect(ip, port).sync().channel();
//        CHANNEL_MAP.put(key, channel);
//        return channel;
//    }
//
//}
