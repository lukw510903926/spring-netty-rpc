package com.spring.netty.common.remote;

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
public class NettyResponse implements Serializable {

    private static final long serialVersionUID = 5963497554185024809L;

    private String responseId;

    private Object data;

    private Boolean success;

    private Exception exception;

    public void setException(Exception exception) {
        this.exception = exception;
        this.setSuccess(false);
    }
}
