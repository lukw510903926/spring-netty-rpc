package com.spring.netty.common.constants;

/**
 * @description: provider 事件类型
 * @email:lkw510903926@163.com
 * @author: yangqi 
 * @since: 2019-08-03 10:28:31
*/
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