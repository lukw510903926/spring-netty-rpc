package com.spring.netty.common.server;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.spring.netty.common.exception.ProviderException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class RedisRegister implements Register{

    private  RedisTemplate<String,String> redisTemplate;

    private final String KEY_PREFIX = "reids:provider:";

    @Override
    public void register(String interfaceName,ProviderInfo providerInfo) {
        
        String key = KEY_PREFIX + interfaceName;
        String provider = redisTemplate.opsForValue().get(key);
        List<ProviderInfo> list;
        if(StringUtils.isNotEmpty(provider)){
            list = JSONObject.parseArray(provider, ProviderInfo.class);
        }else{
            list = new ArrayList<>(16);
        }
        list.add(providerInfo);
        log.info("register interfaceName {}",interfaceName);
        redisTemplate.opsForValue().set(KEY_PREFIX + interfaceName, JSONObject.toJSONString(list));
    }

    @Override
    public List<ProviderInfo> subscribe(String interfaceName) {

        String key = KEY_PREFIX + interfaceName;
        String provider = redisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(provider)){
            throw new ProviderException("provider is not exist");
        }
        return JSONObject.parseArray(provider, ProviderInfo.class);
    }
}