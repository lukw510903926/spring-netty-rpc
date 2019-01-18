package com.spring.netty.server.config;

import com.spring.netty.common.server.NettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 11:12
 **/
@Configuration
public class NettyConfig {

    @Bean
    public NettyServer nettyServer() {
        return new NettyServer();
    }
}
