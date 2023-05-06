/*
 *
 */
package com.cutefool.commons.netty.client;

import io.netty.channel.Channel;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.netty.client.socket.SocketClient;

import java.util.concurrent.TimeUnit;

/**
 * @author 271007729@qq.com
 * @date 9/14/21 5:57 PM
 */
public class Tests {

    public static void main(String[] args) throws InterruptedException {

//        Channel channel = ChannelContext.newInstance((s) -> {
//            System.out.println("netty client  " + s);
//            return Response.ok("netty sever success");
//        }).getChannel("127.0.0.1", 9090);

        SocketClient client = new SocketClient("127.0.0.1", 9090, (s) -> {
            System.out.println(s);
            return Response.ok();
        });

        Channel channel = client.start();

        while (true) {
            boolean say_hello = channel.writeAndFlush("say hello").sync().isDone();
            System.out.println(say_hello);
            TimeUnit.SECONDS.sleep(10);
        }
    }
}
