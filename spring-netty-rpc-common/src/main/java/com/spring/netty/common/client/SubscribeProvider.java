package com.spring.netty.common.client;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import com.spring.netty.common.annotation.Client;
import com.spring.netty.common.exception.ProviderException;
import com.spring.netty.common.register.Register;
import com.spring.netty.common.server.ProviderInfo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 订阅 provider
 * @email:lkw510903926@163.com
 * @author: yangqi
 * @since: 2019-07-27 22:40:03
 */
@Data
@Slf4j
public class SubscribeProvider implements BeanPostProcessor {

    public static Map<String, ProviderInfo> providerMap = new ConcurrentHashMap<>(16);

    private Register register;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Field[] fields = bean.getClass().getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Client.class)) {
                        Class<?> type = field.getType();
                        ProviderInfo instance = register.subscribe(type.getCanonicalName());
                        if (instance == null) {
                            throw new ProviderException(type + " provider is not exist");
                        }
                        providerMap.put(type.getCanonicalName(), instance);
                    }
                }
            } catch (Exception e) {
                System.exit(-1);
                log.error("subscribe provider  error : {}", e);
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}