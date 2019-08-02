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

    private String channel;

    ChannelEnum(String channel){
        this.channel = channel;
    }

   public String  getChannel(){
        return this.channel;
    }
}