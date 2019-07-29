package com.spring.netty.common.client;

import com.spring.netty.common.client.Client;
import com.spring.netty.common.client.NettyClient;
import com.spring.netty.common.exception.ProviderException;
import com.spring.netty.common.server.HostInfo;
import com.spring.netty.common.server.ProviderInfo;
import org.apache.commons.collections4.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * @author yangqi
 * @Description
 *              </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 11:19
 **/
public class ClientManger {

    private static Map<String, List<Client>> clientMap = new ConcurrentHashMap<>(16);

    public static Client getClient(String interfaceName) {

        List<Client> clients = clientMap.get(interfaceName);
        if (CollectionUtils.isEmpty(clients)) {
            createClient(interfaceName);
            clients = clientMap.get(interfaceName);
        }
        clients = clients.stream().filter(Client::isActive).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(clients)) {
            createClient(interfaceName);
            clients = clientMap.get(interfaceName);
        }
        return clients.get(ThreadLocalRandom.current().nextInt(clients.size()));
    }

    private static void createClient(String interfaceName) {

        ProviderInfo providers = SubscribeProvider.providerMap.get(interfaceName);
        if (providers == null || providers.isEmpty()) {
            throw new ProviderException(interfaceName + "provider is not exist");
        }
        List<Client> clients = new ArrayList<>(10);
        providers.getProviderMap().forEach((host, provider) -> {
            HostInfo HostInfo = provider.getHost();
            Client client = new NettyClient(HostInfo.getIp(), HostInfo.getPort());
            clients.add(client);
        });
        clientMap.put(interfaceName, clients);
    }
}
