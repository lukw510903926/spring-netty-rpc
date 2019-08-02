package com.spring.netty.common.register;

import com.spring.netty.common.server.ProviderInfo;

/**
 * @description:
 * @email:lkw510903926@163.com
 * @author: yangqi
 * @since: 2019-07-26 23:13:06
 */
public interface Register {

    /**
     * @description: 注册方法
     * @author: yangqi
     * @since: 2019-07-26 23:15:31
     */
    void register();

    /**
     * 发布事件
     * @param interfaceName
     */
    void push(String interfaceName);

    /**
     * @description: 订阅
     * @author: yangqi
     * @since: 2019-07-26 23:16:11
     */
    ProviderInfo subscribe(String interfaceName);

    /**
     * 订阅事件 
     */
    void subscribeEvent();

    /**
     * @description: 接口注销
     * @author: yangqi
     * @since: 2019-07-27 15:50:11
     */
    void logout();
}