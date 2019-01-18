package com.spring.netty.common.server;

import com.alibaba.fastjson.JSONObject;
import com.spring.netty.common.remote.NettyRequest;
import com.spring.netty.common.remote.NettyResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> instanceMap;

    NettyServerHandler(Map<String, Object> instanceMap){
        this.instanceMap = instanceMap;
    }

    /*
     * channelAction
     *
     * channel 通道 action 活跃的
     *
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     *
     */
    public void channelActive(ChannelHandlerContext ctx) {
        log.info(ctx.channel().localAddress().toString() + " 通道已激活！");
    }

    /*
     * channelInactive
     *
     * channel 通道 Inactive 不活跃的
     *
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     *
     */
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info(ctx.channel().localAddress().toString() + " 通道不活跃！");
    }

    /**
     * 功能：读取服务器发送过来的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 第一种：接收字符串时的处理
        log.info("服务端收到数据 : {}",  msg);
        NettyRequest nettyRequest = JSONObject.parseObject(msg.toString(), NettyRequest.class);
        Object instance = instanceMap.get(nettyRequest.getInterfaceName());
        Method method = instance.getClass().getMethod(nettyRequest.getMethodName(), nettyRequest.getParameterTypes());
        Object result = method.invoke(instance, nettyRequest.getArgs());
        NettyResponse response = new NettyResponse();
        response.setData(result);
        response.setResponseId(nettyRequest.getRequestId());
        ctx.writeAndFlush(JSONObject.toJSONString(response));
    }

    /**
     * 功能：读取完毕客户端发送过来的数据之后的操作
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        log.info("服务端接收数据完毕..");
        // 第一种方法：写一个空的buf，并刷新写出区域。完成后关闭sock channel连接。
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
        // ctx.flush();
        // ctx.flush(); //
        // 第二种方法：在client端关闭channel连接，这样的话，会触发两次channelReadComplete方法。
        // ctx.flush().close().sync(); // 第三种：改成这种写法也可以，但是这中写法，没有第一种方法的好。
    }

    /**
     * 功能：服务端发生异常的操作
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error("server has error : {}", cause);
        ctx.close();
    }
}
