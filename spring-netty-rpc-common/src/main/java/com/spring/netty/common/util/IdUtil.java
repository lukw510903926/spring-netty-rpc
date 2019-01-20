package com.spring.netty.common.util;

import java.util.UUID;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 11:00
 **/
public class IdUtil {

    public static String uuid(){

        return UUID.randomUUID().toString().replaceAll("-","");
    }
}

