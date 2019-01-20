package com.spring.netty.common.client;

import com.alibaba.fastjson.JSONObject;
import com.spring.netty.common.remote.DefaultFuture;
import com.spring.netty.common.remote.NettyResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 向服务端发送数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        log.info("channel 通道激活");
    }

    /**
     * channelInactive
     * <p>
     * channel 通道 Inactive 不活跃的
     * <p>
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("客户端与服务端通道-关闭：" + ctx.channel().localAddress() + "channelInactive");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {

        ByteBuf buf = msg.readBytes(msg.readableBytes());
        String value = buf.toString(Charset.forName("utf-8"));
        NettyResponse response = JSONObject.parseObject(value, NettyResponse.class);
        DefaultFuture.accept(response);
        log.info("客户端接收到的服务端信息:{}", value);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        log.info("异常退出:" + cause.getMessage());
    }
}
