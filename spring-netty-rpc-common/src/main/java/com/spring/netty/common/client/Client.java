package com.spring.netty.common.client;

import com.spring.netty.common.remote.NettyRequest;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 13:44
 **/
public interface Client {

    void request(NettyRequest nettyRequest);
}
