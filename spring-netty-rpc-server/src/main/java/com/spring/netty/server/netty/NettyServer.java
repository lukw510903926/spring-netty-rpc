package com.spring.netty.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/16 14:05
 **/
@Slf4j
@Component
public class NettyServer implements InitializingBean {

    @Override
    public void afterPropertiesSet() {

        try {
            startNettyServer();
        } catch (InterruptedException e) {
            System.exit(-1);
            log.error("netty 容器启动失败 : {}", e);
        }
    }

    private void startNettyServer() throws InterruptedException {

        log.info("启动netty容器");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        int port = 8888;
        bootstrap.bind("127.0.0.1", port).sync();
        log.info("netty server started on port {}", port);
    }
}
