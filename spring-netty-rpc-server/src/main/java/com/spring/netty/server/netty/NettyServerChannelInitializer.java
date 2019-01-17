package com.spring.netty.server.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/17 13:53
 **/
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel channel) {
        channel.pipeline()
                //编码
                .addLast(new StringDecoder())
                //解码
                .addLast(new StringEncoder())
                //handler
                .addLast(new NettyServerHandler());
    }
}