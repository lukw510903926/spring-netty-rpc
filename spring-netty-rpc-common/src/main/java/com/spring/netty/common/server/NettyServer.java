package com.spring.netty.common.server;

import com.spring.netty.common.annotation.Provider;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/16 14:05
 **/
@Slf4j
public class NettyServer implements InitializingBean, ApplicationContextAware {

    private Map<String, Object> providerMap = new HashMap<>();

    private int port;

    private String host;

    private static final int LOCAL_PORT = 8765;

    private static final String LOCAL_HOST = "127.0.0.1";

    public NettyServer() {
        this(LOCAL_HOST, LOCAL_PORT);
    }

    public NettyServer(String host, int port) {

        this.host = StringUtils.isBlank(host) ? LOCAL_HOST : host;
        this.port = port == 0 ? LOCAL_PORT : port;
    }

    @Override
    public void afterPropertiesSet() {

        startNettyServer();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Provider.class);
        if (MapUtils.isNotEmpty(beans)) {
            beans.values().forEach(bean -> {
                Class<?>[] interfaces = bean.getClass().getAnnotation(Provider.class).value();
                if (ArrayUtils.isEmpty(interfaces) || interfaces[0] == void.class) {
                    interfaces = bean.getClass().getInterfaces();
                }
                if (ArrayUtils.isNotEmpty(interfaces)) {
                    Arrays.stream(interfaces).forEach(entity -> providerMap.put(entity.getName(), bean));
                }
            });
        }
    }

    private void startNettyServer() {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            log.info("启动netty容器");
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerChannelInitializer(providerMap))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.bind(this.host, this.port).sync();
            log.info("netty server started on host:{}  port {}", this.host, this.port);
        } catch (InterruptedException e) {
            System.exit(-1);
            log.error("netty 容器启动失败 : {}", e);
        }
    }
}
