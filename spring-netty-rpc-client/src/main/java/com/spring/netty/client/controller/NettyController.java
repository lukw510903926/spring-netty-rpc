package com.spring.netty.client.controller;

import com.spring.netty.api.PersonService;
import com.spring.netty.common.annotation.Client;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/17 17:48
 **/
@RestController
@RequestMapping("/netty")
public class NettyController {

    @Client
    private PersonService personService;

    @GetMapping("proxy")
    public Object proxy(){

        return personService.list();
    }
}
