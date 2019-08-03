package com.spring.netty.common.util;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.MapUtils;

/**
 * @description: token 生成工具
 * @email:lkw510903926@163.com
 * @author: yangqi
 * @since: 2019-08-03 20:20:41
 */
public class TokenUtil{


    /**
     * 根据map生成md5 签名
     * @param params
     * @param solt
     * @return
     */
    public static String genderToken(Map<String,Object> params,String solt) {
        
        StringBuilder builder = new StringBuilder();
        if(MapUtils.isNotEmpty(params)){
            Map<String,Object> treeMap = new TreeMap<>(params);
            treeMap.forEach((key,value) -> builder.append(key).append('=').append(value).append('&'));
        }
        builder.append("$solt").append('=').append(solt);
        return DigestUtils.md5Hex(builder.toString());
    }
}