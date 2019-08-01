package com.spring.netty.common.constants;

public enum RegisterEnventType{
    
    /**
     * 注册接口
     */
    REGISTER("register"),

    /**
     * 下线接口
     */
    UN_REGISTER("unRegister")
    ;

    private String desc;

    RegisterEnventType(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }
}