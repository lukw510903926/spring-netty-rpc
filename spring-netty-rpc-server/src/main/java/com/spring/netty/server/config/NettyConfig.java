package com.spring.netty.server.config;

import com.spring.netty.common.server.NettyServer;
import com.spring.netty.common.server.RedisRegister;
import com.spring.netty.common.server.Register;
import com.spring.netty.common.util.IpUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>
 *
 * @author yangqi
 * @Description
 *              </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 11:12
 **/
@Configuration
public class NettyConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public NettyServer nettyServer() {
        return new NettyServer(IpUtils.localHost(),8765);
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
