package com.spring.netty.common.server;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
@ConditionalOnClass(Jedis.class)
public class RegisterAutoConfiguration{

    @Bean
    public Register register() {

        RedisRegister register = new RedisRegister();
        return register;
    }
}