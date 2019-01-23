package com.spring.netty.common.client;

import com.alibaba.fastjson.JSONObject;
import com.spring.netty.common.remote.DefaultFuture;
import com.spring.netty.common.remote.NettyRequest;
import com.spring.netty.common.util.ClientManger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import java.net.InetSocketAddress;

@Slf4j
public class NettyClient implements  Client {

    private String host;

    private int port;

    private Channel channel;

    private static final String LOCAL_HOST = "127.0.0.1";

    public NettyClient() {
        this(LOCAL_HOST, 8888);
    }

    public NettyClient(int port) {
        this(LOCAL_HOST, port);
    }

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        create();
        ClientManger.addClient(this);
    }

    @Override
    public boolean isActive() {

        return this.channel.isActive();
    }

    private Channel start(EventLoopGroup group, Bootstrap bootstrap) {

        try {
            bootstrap.group(group) // 注册线程池
                    // 使用NioSocketChannel来作为连接用的channel类
                    .channel(NioSocketChannel.class)
                    // 绑定连接端口和host信息
                    .remoteAddress(new InetSocketAddress(this.host, this.port))
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new NettyClientChannelInitializer());
            // 异步连接服务器
            ChannelFuture channelFuture = bootstrap.connect().sync();
            return channelFuture.channel();
        } catch (InterruptedException e) {
            log.info("netty 容器启动失败 ： {}", e);
            System.exit(-1);
        }
        return null;
    }

    @Override
    public Object request(NettyRequest nettyRequest) {

        this.channel.writeAndFlush(JSONObject.toJSONString(nettyRequest));
        DefaultFuture future = new DefaultFuture(nettyRequest, 10 * 1000);
        return future.get().getData();
    }

    private void create() {

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        channel = this.start(group, bootstrap);
    }
}
