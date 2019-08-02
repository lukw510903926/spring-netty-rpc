package com.spring.netty.common.remote;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @email:lkw510903926@163.com
 * @author: yangqi
 * @since: 2019-08-02 23:46:08
 */
@Data
@Slf4j
public class RpcContext {

    private static final ThreadLocal<Map<String, Object>> context = ThreadLocal
            .withInitial(() -> new ConcurrentHashMap<>());

    public static void addParameter(String parameter, Object value) {
        log.info("addParameter parameter : {} value : {}", parameter, value);
        context.get().put(parameter, value);
    }

    public static Object getValue(String parameter) {
        return context.get().get(parameter);
    }

    public static void addAll(Map<String, Object> params) {
        context.get().putAll(params);
    }

    public static Map<String, Object> getContext() {
        return context.get();
    }

    public static void deleteParameter(String parameter) {
        context.get().remove(parameter);
    }
}