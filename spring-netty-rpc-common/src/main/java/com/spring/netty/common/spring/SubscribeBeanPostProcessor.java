package com.spring.netty.common.spring;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import com.spring.netty.common.annotation.Provider;
import com.spring.netty.common.server.HostInfo;
import com.spring.netty.common.server.ProviderInfo;
import com.spring.netty.common.server.Register;
import com.spring.netty.common.util.IpUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import lombok.Setter;

/**
 * @description: 订阅注册中心的接口
 * @author: yangqi
 * @since: 2019-07-27 13:54:57
 */
@Setter
public class SubscribeBeanPostProcessor implements BeanPostProcessor {

    private Register register;

    private HostInfo host;

    private Integer port;

    @PostConstruct
    public void init() {
        this.host = new HostInfo();
        host.setIp(IpUtils.localHost());
        host.setPort(port);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Provider provider = bean.getClass().getAnnotation(Provider.class);
        if (provider != null) {
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            String interfaceName = bean.getClass().getSimpleName();
            ProviderInfo providerInfo = new ProviderInfo();
            providerInfo.setHost(host);
            List<Method> list = new ArrayList<>();
            if (ArrayUtils.isNotEmpty(interfaces)) {
                for (Class<?> instance : interfaces) {
                    Method[] methods = instance.getMethods();
                    list.addAll(Arrays.asList(methods));
                }
            }
            providerInfo.setMethods(list);
            register.register(interfaceName, providerInfo);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}