package com.spring.netty.common.util;

import com.spring.netty.common.client.Client;
import com.spring.netty.common.client.NettyClient;
import org.apache.commons.collections4.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/18 11:19
 **/
public class ClientManger {

    private static List<Client> list = new ArrayList<>();

    private static final Object LOCK = Object.class;

    public static void addClient(Client client) {

        synchronized (LOCK) {
            list.add(client);
        }
    }

    public static Client getClient() {

        if (CollectionUtils.isEmpty(list)) {
            NettyClient client = new NettyClient();
            addClient(client);
            return client;
        }
        list = list.stream().filter(Client::isActive).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            NettyClient client = new NettyClient();
            addClient(client);
            return client;
        }
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}
