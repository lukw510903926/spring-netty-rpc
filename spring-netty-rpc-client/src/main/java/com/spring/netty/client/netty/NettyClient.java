package com.spring.netty.client.netty;

import java.net.InetSocketAddress;

import com.alibaba.fastjson.JSONObject;
import com.spring.netty.remote.NettyRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class NettyClient implements InitializingBean {

    private String host = "127.0.0.1";

    private int port = 8888;

    private Channel channel;

    public NettyClient() {
        this(0);
    }

    public NettyClient(int port) {
        this("localhost", port);
    }

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Channel start() throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group) // 注册线程池
                .channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
                .remoteAddress(new InetSocketAddress(this.host, this.port)) // 绑定连接端口和host信息
                .handler(new NettyClientChannelInitializer());
        ChannelFuture channelFuture = b.connect().sync(); // 异步连接服务器
        return channelFuture.channel();
    }

    public void request(NettyRequest nettyRequest) {

        this.channel.writeAndFlush(JSONObject.toJSONString(nettyRequest));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NettyClient client = new NettyClient("127.0.0.1", 8888);
        channel = client.start();
    }
}
