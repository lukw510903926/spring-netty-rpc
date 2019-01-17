package com.spring.netty.remote;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/17 15:36
 **/
@Data
public class NettyRequest implements Serializable {

    private static final long serialVersionUID = -8878608606655844434L;

    private String requestId;

    private String interfaceName;

    private String methodName;

    private Object[] args;

    private Class<?>[] parameterTypes;
}
