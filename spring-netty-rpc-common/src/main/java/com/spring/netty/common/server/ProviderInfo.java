package com.spring.netty.common.server;

import java.lang.reflect.Method;
import java.util.List;

import lombok.Data;

/*
 * @description: 
 * @author: yangqi 
 * @since: 2019-07-27 13:27:13
*/
@Data
public class ProviderInfo {

    /**
     * 主机信息
     */
    private HostInfo host;

    /**
     * 方法列表
     */
    private List<Method> methods;
}