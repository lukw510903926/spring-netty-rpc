package com.spring.netty.common.server;

import lombok.Data;

/**
 * @description: 主机信息
 * @email:lkw510903926@163.com
 * @author: yangqi 
 * @since: 2019-07-26 23:45:45
*/
@Data
public class HostInfo{

    private String ip;

    private Integer port;
}