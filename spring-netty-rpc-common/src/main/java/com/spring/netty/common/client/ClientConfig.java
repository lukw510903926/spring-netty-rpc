package com.spring.netty.common.client;

import com.spring.netty.common.annotation.Client;
import com.spring.netty.common.proxy.ProxyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 11:37
 **/
@Slf4j
public class ClientConfig implements BeanPostProcessor {

    private Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Field[] fields = bean.getClass().getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Client.class)) {
                        Class<?> type = field.getType();
                        Object instance = instanceMap.get(type);
                        if (instance == null) {
                            instance = ProxyUtil.create(type);
                            instanceMap.put(type, instance);
                        }
                        field.setAccessible(true);
                        field.set(bean, instance);
                    }
                }
            } catch (Exception e) {
                System.exit(-1);
                log.error("注入失败 : {}", e);
            }
        }
        return bean;
    }
}
