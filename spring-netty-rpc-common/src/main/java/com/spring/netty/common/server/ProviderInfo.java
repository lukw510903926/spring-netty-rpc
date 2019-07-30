package com.spring.netty.common.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections4.MapUtils;
import lombok.Data;

/*
 * @description: 
 * @author: yangqi 
 * @since: 2019-07-27 13:27:13
*/
@Data
public class ProviderInfo {

    private Map<String, ProviderBean> providerMap;

    /**
     * @description: 添加provider
     * @author: yangqi
     * @since: 2019-07-29 20:00:42
     */
    public void addProvoder(ProviderBean providerBean) {

        if (MapUtils.isEmpty(providerMap)) {
            providerMap = new ConcurrentHashMap<>(10);
        }
        providerMap.put(providerBean.getServerAddress(), providerBean);
    }

    /**
     * @description: 移除provider
     * @author: yangqi
     * @since: 2019-07-29 20:00:59
     */
    public void removeProvider(HostInfo host) {

        if (MapUtils.isEmpty(providerMap)) {
            return;
        }
        providerMap.remove(host.getHost());
    }

    public boolean isEmpty() {
        return MapUtils.isEmpty(providerMap);
    }
}