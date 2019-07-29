package com.spring.netty.common.server;

import com.spring.netty.common.util.IpUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(Register.class)
@ConditionalOnProperty(prefix = "spring.netty.provider", name = "register", havingValue = "true")
public class ProviderAutoConfiguration {

    @Bean
    public ProviderProperties properties() {
        return new ProviderProperties();
    }

    @Bean
    public NettyServer nettyServer(ProviderProperties properties) {
        return new NettyServer(IpUtils.localHost(), properties.getPort());
    }
}