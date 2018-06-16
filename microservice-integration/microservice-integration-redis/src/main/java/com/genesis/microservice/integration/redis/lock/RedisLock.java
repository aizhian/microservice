package com.genesis.microservice.integration.redis.lock;

/**
 * Created by Aizhanglin on 2017/8/18.
 */
public interface RedisLock {
    void lock(String key);
    void unlock(String key);
}
