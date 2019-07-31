package com.spring.netty.common.register;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "spring.netty.register")
public class RegisterProperties{

    /**
     * 注册中心类型
     */
    private String type;

    /**
     * 注册中心IP
     */
    private String host;

    /**
     * 注册中心端口
     */
    private Integer port;

    /**
     * 超时
     */
    private Integer timeOut;

    /**
     * 注册中心根节点
     */
    private String root;
}