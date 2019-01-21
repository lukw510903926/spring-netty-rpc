package com.spring.netty.common.remote;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class DefaultFuture {

    private static final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap<>();

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private NettyResponse response;

    private long created = System.currentTimeMillis();

    /**
     * 默认超时 10秒
     */
    private long timeOut;

    private void setResponse(NettyResponse response) {
        this.response = response;
    }

    public DefaultFuture(NettyRequest request, long timeOut) {

        this.timeOut = timeOut;
        futureMap.put(request.getRequestId(), this);
    }

    public static void accept(NettyResponse response) {

        DefaultFuture future = futureMap.get(response.getResponseId());
        if (future != null) {
            future.lock.lock();
            try {
                future.setResponse(response);
                //唤醒线程
                future.condition.signal();
                futureMap.remove(response.getResponseId());
            } catch (Exception e) {
                log.error("设置response失败 : {}", e);
            } finally {
                future.lock.unlock();
            }
        }
    }

    public NettyResponse get() {

        lock.lock();
        try {
            while (!done()) {
                if (System.currentTimeMillis() - created > timeOut) {
                    response = new NettyResponse();
                    response.setException(new TimeoutException());
                    break;
                }
                condition.await(timeOut, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            log.error("获取response 失败 : {}", e);
        } finally {
            lock.unlock();
        }
        return this.response;
    }

    private boolean done() {

        return this.response != null;
    }
}
