package com.spring.netty.client.controller;

import com.spring.netty.api.PersonService;
import com.spring.netty.common.annotation.Client;
import com.spring.netty.common.client.NettyClient;
import com.spring.netty.common.proxy.ProxyUtil;
import com.spring.netty.common.remote.NettyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/17 17:48
 **/
@Client
@RestController
@RequestMapping("/netty")
public class NettyController {

    @Autowired
    private NettyClient nettyClient;

    @Client
    private PersonService personService;

    @GetMapping("list")
    public String list(){

        NettyRequest nettyRequest = new NettyRequest();
        nettyRequest.setRequestId(UUID.randomUUID().toString());
        nettyRequest.setArgs(new Object[]{});
        nettyRequest.setInterfaceName(PersonService.class.getName());
        nettyRequest.setMethodName("list");
        nettyRequest.setParameterTypes(new Class<?>[]{});
        nettyClient.request(nettyRequest);
        return "success";
    }

    @GetMapping("proxy")
    public String proxy(){

        personService.list();
        return "success";
    }

    @GetMapping("getByName")
    public String getByName(){

        NettyRequest nettyRequest = new NettyRequest();
        nettyRequest.setRequestId(UUID.randomUUID().toString());
        nettyRequest.setArgs(new Object[]{"Jackson"});
        nettyRequest.setInterfaceName(PersonService.class.getName());
        nettyRequest.setMethodName("getByName");
        nettyRequest.setParameterTypes(new Class<?>[]{String.class});
        nettyClient.request(nettyRequest);
        return "success";
    }
}
