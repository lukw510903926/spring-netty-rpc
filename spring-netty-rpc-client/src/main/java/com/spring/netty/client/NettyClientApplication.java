package com.spring.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/17 17:29
 **/
@SpringBootApplication
public class NettyClientApplication {

    private static Logger logger = LoggerFactory.getLogger(NettyClientApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(NettyClientApplication.class, args);
        logger.info("netty client application startTimeMillis successfully");
    }
}
