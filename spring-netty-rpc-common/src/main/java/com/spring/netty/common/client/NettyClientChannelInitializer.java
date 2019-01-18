package com.spring.netty.common.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.nio.charset.Charset;

public class NettyClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	// 绑定连接初始化器
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
		ch.pipeline().addLast(new NettyClientHandler());
		ch.pipeline().addLast(new ByteArrayEncoder());
		ch.pipeline().addLast(new ChunkedWriteHandler());
	}
}
