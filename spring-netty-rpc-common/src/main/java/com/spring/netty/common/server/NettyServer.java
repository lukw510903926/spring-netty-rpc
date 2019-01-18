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

    @Override
    public void afterPropertiesSet() {

        try {
            startNettyServer();
        } catch (InterruptedException e) {
            System.exit(-1);
            log.error("netty 容器启动失败 : {}", e);
        }
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

    private void startNettyServer() throws InterruptedException {

        log.info("启动netty容器");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerChannelInitializer(providerMap))
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        int port = 8888;
        bootstrap.bind("127.0.0.1", port).sync();
        log.info("netty server started on port {}", port);
    }
}
