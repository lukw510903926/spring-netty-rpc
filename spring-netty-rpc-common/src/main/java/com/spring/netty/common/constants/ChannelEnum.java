package com.spring.netty.common.constants;

public enum ChannelEnum{

     /**
     * 注册
     */
    REGISTER("register"),

    /**
     * 订阅
     */
    SUBSCRIBE("subscribe");

    private String desc;

    ChannelEnum(String desc){
        this.desc = desc;
    }

   public String  getDesc(){
        return this.desc;
    }
}