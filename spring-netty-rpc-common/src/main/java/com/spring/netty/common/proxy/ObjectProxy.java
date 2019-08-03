package com.spring.netty.common.proxy;

import com.spring.netty.common.remote.NettyRequest;
import com.spring.netty.common.remote.RpcContext;
import com.spring.netty.common.client.ClientManger;
import com.spring.netty.common.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 10:40
 **/
@Slf4j
public class ObjectProxy<T> implements InvocationHandler {

    private Class<T> clazz;

    public ObjectProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args){

        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) +
                        ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }
        NettyRequest request = new NettyRequest();
        request.setId(IdUtil.uuid());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setArgs(args);
        request.setInterfaceName(clazz.getName());
        request.setContext(RpcContext.getContext());
        log.info("request : {}", request);
        return ClientManger.getClient(clazz.getCanonicalName()).request(request);
    }
}
