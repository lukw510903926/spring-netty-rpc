package com.spring.netty.server.config;

import com.spring.netty.common.register.RedisRegister;
import com.spring.netty.common.register.Register;

import org.springframework.context.annotation.Bean;

/**
 * <p>
 *
 * @author yangqi
 * @Description
 *              </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 11:12w
 **/
// @Configuration
public class NettyConfig {

    @Bean
    public Register register() {

        RedisRegister register = new RedisRegister();
        return register;
    }
}
