package com.spring.netty.common.client;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	/**
	 * 向服务端发送数据
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		log.info("channel 通道激活");
	}

	/**
	 * channelInactive
	 *
	 * channel 通道 Inactive 不活跃的
	 *
	 * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
	 *
	 */
	public void channelInactive(ChannelHandlerContext ctx) {
		log.info("客户端与服务端通道-关闭：" + ctx.channel().localAddress() + "channelInactive");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg){
		ByteBuf buf = msg.readBytes(msg.readableBytes());
		String value = buf.toString(Charset.forName("utf-8"));
		String responseId = JSONObject.parseObject(value).getString("responseId");
		ctx.channel().attr(AttributeKey.valueOf(responseId)).set(value);
		log.info("客户端接收到的服务端信息:{}" , value);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		ctx.close();
		log.info("异常退出:" + cause.getMessage());
	}
}
