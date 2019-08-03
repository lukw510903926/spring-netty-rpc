package com.spring.netty.common.constants;

/**
 * @description: redis 发布订阅频道
 * @email:lkw510903926@163.com
 * @author: yangqi 
 * @since: 2019-08-03 10:28:09
*/
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