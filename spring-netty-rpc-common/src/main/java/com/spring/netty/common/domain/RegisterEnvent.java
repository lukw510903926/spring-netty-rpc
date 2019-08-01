package com.spring.netty.common.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class RegisterEnvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 注册
     */
    public static final String register = "register";

    /**
     * 下线
     */
    public static final String unregister = "unregister";

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 时间
     */
    private String envent;

}