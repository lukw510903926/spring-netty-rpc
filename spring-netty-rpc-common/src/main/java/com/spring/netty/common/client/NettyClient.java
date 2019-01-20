package com.spring.netty.common.client;

import com.alibaba.fastjson.JSONObject;
import com.spring.netty.common.listener.ConnectionListener;
import com.spring.netty.common.remote.NettyRequest;
import com.spring.netty.common.util.ClientManger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.net.InetSocketAddress;

@Slf4j
public class NettyClient implements InitializingBean, Client {

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

    @Override
    public Channel start(EventLoopGroup group, Bootstrap bootstrap) {

        try {
            bootstrap.group(group) // 注册线程池
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
                    .remoteAddress(new InetSocketAddress(this.host, this.port)) // 绑定连接端口和host信息
                    .handler(new NettyClientChannelInitializer());
            // 异步连接服务器
            ChannelFuture channelFuture = bootstrap.connect().addListener(new ConnectionListener(this)).sync();
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
        return this.channel.attr(AttributeKey.valueOf(nettyRequest.getRequestId())).get();
    }

    @Override
    public void afterPropertiesSet() {

        NettyClient client = new NettyClient("127.0.0.1", 8888);
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        channel = client.start(group, bootstrap);
        ClientManger.addClient(this);
    }
}
