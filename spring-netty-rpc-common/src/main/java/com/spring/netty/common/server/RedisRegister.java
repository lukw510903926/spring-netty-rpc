package com.spring.netty.common.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.alibaba.fastjson.JSONObject;
import com.spring.netty.common.annotation.Provider;
import com.spring.netty.common.exception.ProviderException;
import com.spring.netty.common.util.IpUtils;
import java.lang.reflect.Method;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class RedisRegister implements Register, ApplicationContextAware {

    private RedisTemplate<String, String> redisTemplate;

    private final String KEY_PREFIX = "reids:provider:";

    private ApplicationContext applicationContext;

    private HostInfo localHost;

    private Integer port;

    @PostConstruct
    public void init() {
        localHost = new HostInfo();
        localHost.setIp(IpUtils.localHost());
        localHost.setPort(port);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void register() {

        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Provider.class);
        if (MapUtils.isEmpty(beans)) {
            return;
        }
        beans.forEach((key, bean) -> {
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            if (ArrayUtils.isNotEmpty(interfaces)) {
                String interfaceName;
                for (Class<?> instance : interfaces) {
                    interfaceName = instance.getCanonicalName();
                    Method[] methods = instance.getMethods();
                    ProviderInfo providerInfo = new ProviderInfo();
                    providerInfo.setHost(localHost);
                    providerInfo.setMethods(Arrays.asList(methods));
                    String interfaceNameKey = KEY_PREFIX + interfaceName;
                    String provider = redisTemplate.opsForValue().get(interfaceNameKey);
                    List<ProviderInfo> providers;
                    if (StringUtils.isNotEmpty(provider)) {
                        providers = JSONObject.parseArray(provider, ProviderInfo.class);
                    } else {
                        providers = new ArrayList<>(16);
                    }
                    providers.add(providerInfo);
                    log.info("register interfaceName {}", interfaceName);
                    redisTemplate.opsForValue().set(key, JSONObject.toJSONString(providers));
                }
            }
        });
    }

    @Override
    public List<ProviderInfo> subscribe(String interfaceName) {

        String key = KEY_PREFIX + interfaceName;
        String provider = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(provider)) {
            throw new ProviderException("provider is not exist");
        }
        return JSONObject.parseArray(provider, ProviderInfo.class);
    }

    @Override
    public void logout() {

        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Provider.class);
        if (MapUtils.isEmpty(beans)) {
            return;
        }
        beans.forEach((key, bean) -> {
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            if (ArrayUtils.isNotEmpty(interfaces)) {
                String interfaceName;
                for (Class<?> instance : interfaces) {
                    interfaceName = instance.getCanonicalName();
                    String interfaceNameKey = KEY_PREFIX + interfaceName;
                    String provider = redisTemplate.opsForValue().get(interfaceNameKey);
                    if (StringUtils.isEmpty(provider)) {
                        continue;
                    }
                    List<ProviderInfo> list = JSONObject.parseArray(provider, ProviderInfo.class);
                    List<ProviderInfo> reuslt = new ArrayList<>(10);
                    list.forEach(providerInfo -> {
                        HostInfo host = providerInfo.getHost();
                        if (!localHost.equals(host)) {
                            reuslt.add(providerInfo);
                        }
                    });
                    if (CollectionUtils.isNotEmpty(reuslt)) {
                        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
                    } else {
                        redisTemplate.delete(key);
                    }
                }
            }
        });
    }

    @PreDestroy
    public void destroy() {
        logout();
    }
}