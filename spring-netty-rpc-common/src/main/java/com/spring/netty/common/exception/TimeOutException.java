package com.spring.netty.common.exception;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 14:26
 **/
public class TimeOutException extends RuntimeException {

    public TimeOutException() {
        super();
    }

    public TimeOutException(String message) {
        super(message);
    }
}
