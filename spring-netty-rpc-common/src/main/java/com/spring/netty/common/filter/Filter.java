package com.spring.netty.common.filter;

import com.spring.netty.common.remote.NettyResponse;

public interface Filter{

    NettyResponse doFilter(Object object);
}