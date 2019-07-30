package com.spring.netty.common.client;

import com.spring.netty.common.register.Register;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(Register.class)
public class ClientAutoConfiguration {

    @Bean
    public ClientConfig clientConfig(){

        return new ClientConfig();
    }

    @Bean
    public SubscribeProvider subscribeProvider(Register register) {
        
        SubscribeProvider subscribeProvider = new SubscribeProvider();
        subscribeProvider.setRegister(register);
        return subscribeProvider;
    }
}