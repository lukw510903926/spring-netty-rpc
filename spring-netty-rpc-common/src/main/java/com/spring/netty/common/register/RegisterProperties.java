package com.spring.netty.common.register;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "spring.netty.register")
public class RegisterProperties{

    /**
     * 注册中心类型
     */
    private String type =  "redis";

    /**
     * 注册中心IP
     */
    private String host = "127.0.0.1";

    /**
     * 注册中心端口
     */
    private Integer port = 6379;

    /**
     * 超时
     */
    private Integer timeOut = 30;

    /**
     * 注册中心根节点
     */
    private String root = "netty.provider:";
}