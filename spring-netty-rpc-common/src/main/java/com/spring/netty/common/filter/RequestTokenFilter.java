package com.spring.netty.common.filter;

import com.spring.netty.common.constants.Constants;
import com.spring.netty.common.constants.FilterType;
import com.spring.netty.common.remote.NettyRequest;
import com.spring.netty.common.remote.RpcContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @description: client端生成token
 * @email:lkw510903926@163.com
 * @author: yangqi
 * @since: 2019-08-03 10:29:04
 */
public class RequestTokenFilter implements Filter {

    @Override
    public int order() {
        return 0;
    }

    @Override
    public Object doFilter(Object object) {

        NettyRequest request = (NettyRequest) object;
        Object[] args = request.getArgs();
        if (ArrayUtils.isEmpty(args)) {
            String token = DigestUtils.md5Hex(request.getId());
            RpcContext.getContext().put(Constants.TOKEN, token);
        }
        request.setContext(RpcContext.getContext());
        return object;
    }

    @Override
    public String filterType() {
        return FilterType.BEFORE_REQUEST.getType();
    }
}