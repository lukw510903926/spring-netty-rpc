package com.spring.netty.common.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "spring.netty.provider")
public class ProviderProperties {

    /**
     * 注册中心类型
     */
    private String registerType;

    /**
     * 注册中心IP
     */
    private String registerIp;

    /**
     * 注册中心端口
     */
    private Integer registerPort;

    /**
     * 服务提供端口
     */
    private Integer port;

    /**
     * 是否注册接口
     */
    private Boolean register = Boolean.TRUE;

}