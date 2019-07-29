package com.spring.netty.common.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import com.spring.netty.common.annotation.Provider;
import com.spring.netty.common.exception.ProviderException;
import com.spring.netty.common.util.IpUtils;
import java.lang.reflect.Method;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class RedisRegister implements Register, ApplicationContextAware, InitializingBean {

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
                    if (ArrayUtils.isNotEmpty(methods)) {
                        Stream.of(methods).forEach(method -> {
                            methodNames.add(method.getName());
                        });

                    }

                    ProviderBean providerBean = new ProviderBean();
                    providerBean.setMethods(methodNames);
                    providerBean.setHost(localHost);
                    providerBean.setServerAddress(localHost.getHost());

                    String interfaceNameKey = KEY_PREFIX + interfaceName;
                    Object provider = redisTemplate.opsForValue().get(interfaceNameKey);
                    ProviderInfo providerInfo = new ProviderInfo();
                    if (provider != null) {
                        providerInfo = (ProviderInfo) provider;
                    } else {
                        providerInfo = new ProviderInfo();
                    }
                    providerInfo.addProvoder(providerBean);
                    log.info("register interfaceName {}", interfaceName);
                    redisTemplate.opsForValue().set(interfaceNameKey, providerInfo);
                }
            }
        });
    }

    @Override
    public ProviderInfo subscribe(String interfaceName) {

        String key = KEY_PREFIX + interfaceName;
        Object provider = redisTemplate.opsForValue().get(key);
        if (provider == null) {
            throw new ProviderException("provider is not exist");
        }
        return (ProviderInfo) provider;
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
                    ProviderInfo providerInfo = (ProviderInfo) provider;
                    providerInfo.removeProvider(localHost);
                    log.info("begin to logout interfaceName {}", interfaceNameKey);
                    if (providerInfo.isEmpty()) {
                        redisTemplate.delete(interfaceNameKey);
                    } else {
                        redisTemplate.opsForValue().set(interfaceNameKey, providerInfo);
                    }
                }
            }
        });
    }

    @PreDestroy
    public void destroy() {
        logout();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        register();
    }

}