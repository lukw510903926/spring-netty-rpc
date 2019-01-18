package com.spring.netty.client.config;

import com.spring.netty.common.client.ClientConfig;
import com.spring.netty.common.client.NettyClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 11:10
 **/
@Configuration
public class NettyConfig {

    @Bean
    public NettyClient nettyClient(){
        return new NettyClient();
    }

    @Bean
    public ClientConfig clientConfig(){

        return new ClientConfig();
    }
}
