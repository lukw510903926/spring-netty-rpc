package com.spring.netty.common.client;

import com.spring.netty.common.annotation.Client;
import com.spring.netty.common.proxy.ProxyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
public class ClientConfig implements ApplicationContextAware {

    private Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Client.class);
        if (MapUtils.isNotEmpty(beans)) {

            beans.values().forEach(bean -> {
                Field[] fields = bean.getClass().getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    try {
                        for (Field field : Arrays.asList(fields)) {
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
            });
        }
    }
}
