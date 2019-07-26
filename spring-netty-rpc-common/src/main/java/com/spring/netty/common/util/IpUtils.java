package com.spring.netty.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpUtils {

    private IpUtils() {
    }

    public static String localHost() {

        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取本机ip地址失败", e);
        }
        return null;
    }

    public static String getRequestIp(HttpServletRequest request){

        String remoteAddr = request.getRemoteAddr();  
        String forwarded = request.getHeader("X-Forwarded-For");  
        String realIp = request.getHeader("X-Real-IP");  
        String ip = null;  
        if (realIp == null) {  
            if (forwarded == null) {  
                ip = remoteAddr;  
            } else {  
                ip = remoteAddr + "/" + forwarded.split(",")[0];  
            }  
        } else {  
            if (realIp.equals(forwarded)) {  
                ip = realIp;  
            } else {  
                if(forwarded != null){  
                    forwarded = forwarded.split(",")[0];  
                }  
                ip = realIp + "/" + forwarded;  
            }  
        }
        return ip;
    }
}