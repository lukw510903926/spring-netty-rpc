package com.spring.netty.common.register;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "spring.netty.register")
public class RegisterProperties{

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

    private Integer registerTimeOut;
}