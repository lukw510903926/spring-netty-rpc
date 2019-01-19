package com.spring.netty.common.util;

import com.spring.netty.common.client.Client;
import com.spring.netty.common.exception.ChannelException;
import io.netty.channel.Channel;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

    private static final Class<?> LOCK = Object.class;

    public static void addClient(Client client) {

        synchronized (LOCK) {
            list.add(client);
        }
    }

    public static Client getClient() {

        if (CollectionUtils.isEmpty(list)) {
            throw new ChannelException();
        }
        int index = ThreadLocalRandom.current().nextInt(list.size());
        index = index > 0 ? index - 1 : 0;
        return list.get(index);
    }
}