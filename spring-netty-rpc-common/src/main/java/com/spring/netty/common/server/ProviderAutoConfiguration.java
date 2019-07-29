package com.spring.netty.common.server;

import com.spring.netty.common.util.IpUtils;

import org.springframework.context.annotation.Bean;

public class ProviderAutoConfiguration{

    @Bean
    public ProviderProperties properties(){
        return new ProviderProperties();
    }

    @Bean
    public Register register(ProviderProperties properties) {

        RedisRegister register = new RedisRegister();
        register.setPort(properties.getPort());
        return register;
    }

    @Bean
    public NettyServer nettyServer(ProviderProperties properties) {
        return new NettyServer(IpUtils.localHost(),properties.getPort());
    }
}