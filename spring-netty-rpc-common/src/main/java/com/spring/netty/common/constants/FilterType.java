package com.spring.netty.common.constants;

/**
 * @description: filter
 * @email:lkw510903926@163.com
 * @author: yangqi 
 * @since: 2019-08-03 10:27:42
*/
public enum FilterType {

    BEFORE_REQUEST("before_request"),

    AFTER_REQUEST("after_request"),

    BEFORE_RESPONSE("before_response"),

    AFTER_RESPONSE("after_response");

    private String type;

    FilterType(String type) {

        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}