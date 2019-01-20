package com.spring.netty.common.client;

import com.spring.netty.common.remote.NettyRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 13:44
 **/
public interface Client {

    /**
     * 发送请求
     *
     * @param nettyRequest
     * @return
     */
    Object request(NettyRequest nettyRequest);

    /**
     * 创建连接
     *
     * @param group
     * @param bootstrap
     * @return
     */
    Channel start(EventLoopGroup group, Bootstrap bootstrap);
}
