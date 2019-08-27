package com.spring.netty.common.register;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.alibaba.fastjson.JSONObject;
import com.spring.netty.common.annotation.Provider;
import com.spring.netty.common.constants.ChannelEnum;
import com.spring.netty.common.constants.RegisterEventType;
import com.spring.netty.common.domain.RegisterEvent;
import com.spring.netty.common.exception.ProviderException;
import com.spring.netty.common.server.HostInfo;
import com.spring.netty.common.server.ProviderBean;
import com.spring.netty.common.server.ProviderInfo;
import com.spring.netty.common.server.ProviderProperties;
import com.spring.netty.common.util.IpUtils;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Setter
@Slf4j
public class RedisRegister implements Register, ApplicationContextAware, InitializingBean {

    private RegisterProperties registerProperties;

    private ProviderProperties providerProperties;

    private final String KEY_PREFIX = "redis:provider:";

    private ApplicationContext applicationContext;

    private HostInfo localHost;

    private JedisPool jedisPool;

    private String rootPath;

    @PostConstruct
    public void init() {

        localHost = new HostInfo();
        localHost.setIp(IpUtils.localHost());
        localHost.setPort(providerProperties.getPort());
        initJedisPool();

        this.rootPath = registerProperties.getRoot();
        if (StringUtils.isEmpty(rootPath)) {
            rootPath = KEY_PREFIX;
        }
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
        Jedis jedis = jedisPool.getResource();
        beans.forEach((key, bean) -> {
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            if (ArrayUtils.isNotEmpty(interfaces)) {
                String interfaceName;
                for (Class<?> instance : interfaces) {
                    interfaceName = instance.getCanonicalName();
                    Method[] methods = instance.getMethods();
                    List<String> methodNames = new ArrayList<>(10);
                    if (ArrayUtils.isNotEmpty(methods)) {
                        Stream.of(methods).forEach(method -> methodNames.add(method.getName()));
                    }
                    ProviderBean providerBean = new ProviderBean();
                    providerBean.setMethods(methodNames);
                    providerBean.setHost(localHost);
                    providerBean.setServerAddress(localHost.getHost());
                    String interfaceNameKey = rootPath + interfaceName;

                    String provider = jedis.get(interfaceNameKey);
                    ProviderInfo providerInfo;
                    if (StringUtils.isNotEmpty(provider)) {
                        providerInfo = JSONObject.parseObject(provider, ProviderInfo.class);
                    } else {
                        providerInfo = new ProviderInfo();
                    }
                    providerInfo.addProvoder(providerBean);
                    log.info("register interfaceName {}", interfaceName);
                    jedis.set(interfaceNameKey, JSONObject.toJSONString(providerInfo));
                }
            }
        });
        closeJedis(jedis);
    }

    @Override
    public ProviderInfo subscribe(String interfaceName) {

        String key = rootPath + interfaceName;
        Jedis jedis = jedisPool.getResource();
        String provider = jedis.get(key);
        if (StringUtils.isEmpty(provider)) {
            throw new ProviderException("provider is not exist");
        }
        closeJedis(jedis);
        return JSONObject.parseObject(provider, ProviderInfo.class);
    }

    @Override
    public void logout() {

        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Provider.class);
        if (MapUtils.isEmpty(beans)) {
            return;
        }
        Jedis jedis = jedisPool.getResource();
        log.info("begin to logout interfaceName");
        beans.forEach((key, bean) -> {
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            if (ArrayUtils.isNotEmpty(interfaces)) {
                String interfaceName;
                for (Class<?> instance : interfaces) {
                    interfaceName = instance.getCanonicalName();
                    String interfaceNameKey = rootPath + interfaceName;
                    String provider = jedis.get(interfaceNameKey);
                    if (StringUtils.isEmpty(provider)) {
                        continue;
                    }
                    ProviderInfo providerInfo = JSONObject.parseObject(provider, ProviderInfo.class);
                    providerInfo.removeProvider(localHost);
                    log.info("begin to logout interfaceName {}", interfaceNameKey);
                    if (providerInfo.isEmpty()) {
                        jedis.del(interfaceNameKey);
                    } else {
                        jedis.set(interfaceNameKey, JSONObject.toJSONString(providerInfo));
                    }
                }
            }
        });
        closeJedis(jedis);
    }

    @PreDestroy
    public void destroy() {
        logout();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        register();
    }

    private void initJedisPool() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        String host = registerProperties.getHost();
        Integer port = registerProperties.getPort();
        Integer timeOut = registerProperties.getTimeOut();
        jedisPool = new JedisPool(config, host, port, timeOut);
    }

    private void closeJedis(Jedis jedis) {

        if (jedis != null) {
            jedis.close();
        }
    }

    @Override
    public void push(String interfaceName) {

      Jedis jedis =jedisPool.getResource();
      RegisterEvent registerEvent = new RegisterEvent();
      registerEvent.setInterfaceName(interfaceName);
      registerEvent.setEvent(RegisterEventType.REGISTER.getDesc());
      jedis.publish(ChannelEnum.REGISTER.getChannel(), JSONObject.toJSONString(registerEvent));
      closeJedis(jedis);
    }



    @Override
    public void subscribeEvent() {

        Jedis jedis = jedisPool.getResource();
        jedis.subscribe(new SubscribeProvider(), ChannelEnum.REGISTER.getChannel());
        closeJedis(jedis);
    }
}