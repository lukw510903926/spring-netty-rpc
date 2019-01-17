package com.spring.netty.remote;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/17 18:06
 **/
@Data
public class NettyResponse<T> implements Serializable {

    private static final long serialVersionUID = 5963497554185024809L;

    private String responseId;

    private T data;

    private Boolean success;

    private Exception exception;
}
