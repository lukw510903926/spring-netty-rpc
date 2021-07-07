package com.spring.netty.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * @author yangqi
 * @Description
 *              </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/17 17:29
 **/
@Slf4j
@SpringBootApplication
public class NettyClientApplication {

    public static void main(String[] args) {

        SpringApplication.run(NettyClientApplication.class, args);
        log.info("netty client application startTimeMillis successfully");
    }
}
