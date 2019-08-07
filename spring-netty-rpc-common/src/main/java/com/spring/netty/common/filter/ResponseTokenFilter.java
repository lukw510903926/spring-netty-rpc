package com.spring.netty.common.filter;

import com.alibaba.fastjson.JSONObject;
import com.spring.netty.common.constants.Constants;
import com.spring.netty.common.constants.FilterType;
import com.spring.netty.common.exception.RpcException;
import com.spring.netty.common.remote.NettyRequest;
import com.spring.netty.common.remote.Request;
import com.spring.netty.common.util.TokenUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @description:
 * @email:lkw510903926@163.com
 * @author: yangqi
 * @since: 2019-08-03 20:59:08
 */
public class ResponseTokenFilter implements Filter {

    @Override
    public int order() {
        return 0;
    }

    @Override
    public void doFilter(Object object) {
        
        NettyRequest request = (NettyRequest) object;
        Object[] args = request.getArgs();
        String requestToken = (String) request.getContext().get(Constants.TOKEN);
        if (StringUtils.isEmpty(requestToken)) {
            throw new RpcException("the requst token is empty");
        }
        String token;
        if (ArrayUtils.isEmpty(args)) {
            token = DigestUtils.md5Hex(request.getId());
            if (!requestToken.equals(token)) {
                throw new RpcException(" token validate is fail");
            }
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
        if (!requestToken.equals(token)) {
            throw new RpcException(" token validate is fail");
        }
    }

    @Override
    public String filterType() {
        return FilterType.BEFORE_RESPONSE.getType();
    }
}