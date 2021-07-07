package com.spring.netty.server;

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
 * @since 2019/1/17 17:12
 **/
@SpringBootApplication
public class NettyServerApplication {

    private static Logger logger = LoggerFactory.getLogger(NettyServerApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(NettyServerApplication.class,args);
        logger.info("spring netty application startTimeMillis successfully-----");
    }
}
