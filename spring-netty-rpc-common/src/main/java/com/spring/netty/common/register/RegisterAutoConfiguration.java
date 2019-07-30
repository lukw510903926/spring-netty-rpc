package com.spring.netty.common.register;

import com.spring.netty.common.register.RedisRegister;
import com.spring.netty.common.register.Register;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
@ConditionalOnClass(Jedis.class)
public class RegisterAutoConfiguration{

    @Bean
    public RegisterProperties registerProperties() {
        return new RegisterProperties();
    }

    @Bean
    public Register register() {

        RedisRegister register = new RedisRegister();
        register.setRegisterProperties(registerProperties());
        return register;
    }
}