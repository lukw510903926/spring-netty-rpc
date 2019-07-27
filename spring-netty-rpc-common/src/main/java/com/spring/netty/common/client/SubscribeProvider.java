package com.spring.netty.common.client;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import com.spring.netty.common.annotation.Client;
import com.spring.netty.common.exception.ProviderException;
import com.spring.netty.common.server.ProviderInfo;
import com.spring.netty.common.server.Register;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class SubscribeProvider implements BeanPostProcessor {

    public static Map<String, List<ProviderInfo>> providerMap = new HashMap<>(16);

    private Register register;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Field[] fields = bean.getClass().getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Client.class)) {
                        Class<?> type = field.getType();
                        List<ProviderInfo> instance = providerMap.get(type.getCanonicalName());
                        if (CollectionUtils.isNotEmpty(instance)) {
                            continue;
                        }
                        instance = register.subscribe(type.getCanonicalName());
                        if (CollectionUtils.isEmpty(instance)) {
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