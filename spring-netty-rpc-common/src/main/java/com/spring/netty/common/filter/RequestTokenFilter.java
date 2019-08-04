package com.spring.netty.common.filter;

import com.alibaba.fastjson.JSONObject;
import com.spring.netty.common.constants.Constants;
import com.spring.netty.common.constants.FilterType;
import com.spring.netty.common.exception.RpcException;
import com.spring.netty.common.remote.NettyRequest;
import com.spring.netty.common.remote.Request;
import com.spring.netty.common.remote.RpcContext;
import com.spring.netty.common.util.TokenUtil;

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
        String token;
        if (ArrayUtils.isEmpty(args)) {
            token = DigestUtils.md5Hex(request.getId());
            RpcContext.addParameter(Constants.TOKEN, token);
        }
        if (args.length > 1) {
            throw new RpcException("the max args length is 1");
        }
        Object param = args[0];
        if (!Request.class.isAssignableFrom(param.getClass())) {
            throw new RpcException("the args should implements request");
        }
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(param), JSONObject.class);
        token = TokenUtil.genderToken(jsonObject.getInnerMap(), request.getId());
        RpcContext.addParameter(Constants.TOKEN, token);
        return object;
    }

    @Override
    public String filterType() {
        return FilterType.BEFORE_REQUEST.getType();
    }
}