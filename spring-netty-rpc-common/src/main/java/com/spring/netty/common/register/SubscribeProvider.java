package com.spring.netty.common.register;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

@Slf4j
public class SubscribeProvider extends JedisPubSub {

    public SubscribeProvider() {
    }

    /**
     * 收到消息会调用
     */
    @Override
    public void onMessage(String channel, String message) {
        log.info("receive redis published message, channel {}, message {}", channel, message);
    }

    /**
     * 订阅了频道会调用
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        log.info("subscribe redis channel success, channel {}, subscribedChannels {}", channel, subscribedChannels);
    }

    /**
     * 取消订阅 会调用
     */
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        log.info("unsubscribe redis channel, channel {}, subscribedChannels {}", channel, subscribedChannels);
    }
}