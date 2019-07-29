package com.spring.netty.client.config;

import com.spring.netty.common.client.ClientConfig;
import com.spring.netty.common.client.SubscribeProvider;
import com.spring.netty.common.server.RedisRegister;
import com.spring.netty.common.server.Register;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

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

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public ClientConfig clientConfig(){

        return new ClientConfig();
    }

    @Bean
    public SubscribeProvider subscribeProvider() {
        
        SubscribeProvider subscribeProvider = new SubscribeProvider();
        subscribeProvider.setRegister(register());
        return subscribeProvider;
    }

    @Bean
    public Register register() {

        RedisRegister register = new RedisRegister();
        register.setRedisTemplate(redisTemplate());
        return register;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        return redisTemplate;
    }
}
