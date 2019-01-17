package com.spring.netty.client.controller;

import com.spring.netty.api.PersonService;
import com.spring.netty.client.netty.NettyClient;
import com.spring.netty.remote.NettyRequest;
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
@RestController
@RequestMapping("/netty")
public class NettyController {

    @Autowired
    private NettyClient nettyClient;

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
