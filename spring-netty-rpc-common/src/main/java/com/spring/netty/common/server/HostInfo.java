package com.spring.netty.common.server;

import lombok.Data;

/**
 * @description: 主机信息
 * @email:lkw510903926@163.com
 * @author: yangqi
 * @since: 2019-07-26 23:45:45
 */
@Data
public class HostInfo {

    private String ip;

    private Integer port;

    public String getHost() {
        return this.ip + ':' + this.port;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return this.getHost().equals(((HostInfo)obj).getHost());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        return result;
    }

}