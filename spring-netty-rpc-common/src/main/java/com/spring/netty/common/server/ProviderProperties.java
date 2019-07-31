package com.spring.netty.common.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "spring.netty.provider")
public class ProviderProperties {
    
    /**
     * 服务提供端口
     */
    private Integer port = 8765;

    /**
     * 是否注册接口
     */
    private Boolean register = Boolean.TRUE;

}