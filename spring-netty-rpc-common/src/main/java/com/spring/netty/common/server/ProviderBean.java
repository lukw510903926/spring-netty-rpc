package com.spring.netty.common.server;

import java.util.List;

import lombok.Data;

@Data
public class ProviderBean {

    private HostInfo host;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 服务器地址 ip:host
     */
    private String serverAddress;

    /**
     * 方法列表
     */
    private List<String> methods;
}