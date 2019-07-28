package com.spring.netty.common.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class RedisRegister implements Register, ApplicationContextAware {

    private RedisTemplate<String, Object> redisTemplate;

    private final String KEY_PREFIX = "reids:provider:";

    private ApplicationContext applicationContext;

    private HostInfo localHost;

    private Integer port = 8765;

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
                    List<String> methodNames = new ArrayList<>(10);
                    if(ArrayUtils.isNotEmpty(methods)){
                        Stream.of(methods).forEach(method ->{
                            methodNames.add(method.getName());
                        });

                    }
                    ProviderInfo providerInfo = new ProviderInfo();
                    providerInfo.setHost(localHost);
                    providerInfo.setMethods(methodNames);
                    String interfaceNameKey = KEY_PREFIX + interfaceName;
                    Object provider = redisTemplate.opsForValue().get(interfaceNameKey);
                    List<ProviderInfo> providers;
                    if (provider != null) {
                        providers = (List<ProviderInfo>) provider;
                    } else {
                        providers = new ArrayList<>(16);
                    }
                    providers.add(providerInfo);
                    log.info("register interfaceName {}", interfaceName);
                    redisTemplate.opsForValue().set(interfaceNameKey, providers);
                }
            }
        });
    }

    @Override
    public List<ProviderInfo> subscribe(String interfaceName) {

        String key = KEY_PREFIX + interfaceName;
        Object provider = redisTemplate.opsForValue().get(key);
        if (provider == null) {
            throw new ProviderException("provider is not exist");
        }
        return (List<ProviderInfo>) provider;
    }

    @Override
    public void logout() {

        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Provider.class);
        if (MapUtils.isEmpty(beans)) {
            return;
        }
        log.info("begin to logout interfaceName");
        beans.forEach((key, bean) -> {
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            if (ArrayUtils.isNotEmpty(interfaces)) {
                String interfaceName;
                for (Class<?> instance : interfaces) {
                    interfaceName = instance.getCanonicalName();
                    String interfaceNameKey = KEY_PREFIX + interfaceName;
                    Object provider = redisTemplate.opsForValue().get(interfaceNameKey);
                    if (provider == null) {
                        continue;
                    }
                    List<ProviderInfo> list = (List<ProviderInfo>) provider;
                    List<ProviderInfo> reuslt = new ArrayList<>(10);
                    list.forEach(providerInfo -> {
                        HostInfo host = providerInfo.getHost();
                        if (!localHost.equals(host)) {
                            reuslt.add(providerInfo);
                        }
                    });
                    log.info("begin to logout interfaceName {}",interfaceNameKey);
                    if (CollectionUtils.isNotEmpty(reuslt)) {
                        redisTemplate.opsForValue().set(interfaceNameKey, JSONObject.toJSONString(list));
                    } else {
                        redisTemplate.delete(interfaceNameKey);
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