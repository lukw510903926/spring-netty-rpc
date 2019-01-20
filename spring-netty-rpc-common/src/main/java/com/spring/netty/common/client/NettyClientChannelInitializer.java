package com.spring.netty.common.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.nio.charset.StandardCharsets;

public class NettyClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8))
                .addLast(new NettyClientHandler())
                .addLast(new ChunkedWriteHandler())
                .addLast(new ByteArrayEncoder());
    }
}
