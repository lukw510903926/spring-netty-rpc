package com.spring.netty.server.config;

import com.spring.netty.common.server.Register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StartRunner implements CommandLineRunner {

    @Autowired
    private Register register;

    @Override
    public void run(String... args) {

        log.info("springboot启动后执行此方法");
        register.register();
    }
}
