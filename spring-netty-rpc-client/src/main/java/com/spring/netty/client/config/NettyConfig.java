package com.spring.netty.client.config;

import com.spring.netty.common.client.ClientConfig;
import com.spring.netty.common.client.SubscribeProvider;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 *
 * @author yangqi
 * @Description
 * @email yangqi@ywwl.com
 * @since 2019/1/18 11:10
 **/
// @Configuration
public class NettyConfig{

    @Bean
    public ClientConfig clientConfig(){

        return new ClientConfig();
    }

    @Bean
    public SubscribeProvider subscribeProvider() {
        
        SubscribeProvider subscribeProvider = new SubscribeProvider();
        return subscribeProvider;
    }

}
