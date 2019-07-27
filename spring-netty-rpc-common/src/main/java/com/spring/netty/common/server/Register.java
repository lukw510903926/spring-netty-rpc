package com.spring.netty.common.server;

import java.util.List;

/**
 * @description:
 * @email:lkw510903926@163.com
 * @author: yangqi
 * @since: 2019-07-26 23:13:06
 */
public interface Register{

    /**
     * @description: 注册方法
     * @author: yangqi 
     * @since: 2019-07-26 23:15:31
    */
    void register(String interfaceName,ProviderInfo providerInfo);

    /**
     * @description: 订阅
     * @author: yangqi 
     * @since: 2019-07-26 23:16:11
    */
    List<ProviderInfo> subscribe(String interfaceName);
}