package com.spring.netty.common.util;

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
public class ChannelManger {

    private static List<Channel> list = new ArrayList<>();

    private static final Class<?> LOCK = Object.class;

    public static void addChannel(Channel channel) {

        synchronized (LOCK) {
            list.add(channel);
        }
    }

    public static Channel getChannel() {

        if (CollectionUtils.isEmpty(list)) {
            throw new ChannelException();
        }
        return list.get(ThreadLocalRandom.current().nextInt(list.size() - 1));
    }
}
