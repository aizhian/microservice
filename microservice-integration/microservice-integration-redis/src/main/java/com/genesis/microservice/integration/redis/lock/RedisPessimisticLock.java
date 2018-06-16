package com.genesis.microservice.integration.redis.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;

/**
 * Created by Aizhanglin on 2017/8/17.
 * redis同步锁
 */
public class RedisPessimisticLock implements RedisLock {
    private final String lock_prefix="lock:";
    private final int locked_time=30 * 1000;//锁默认存活时间
    private final int retry_timeout=10 * 1000;//获取锁默认超时时长
    @Autowired
    JedisCluster jedisCluster;
//    @Autowired
//    RedisTemplate redisTemplate;


    /**
     * 开启redis悲观锁 setnx命令实现
     * @param key 锁定键
     */
    public void lock(String key){
        long endTime=System.currentTimeMillis()+retry_timeout;
        String lockKey=lock_prefix + ":" + key;
        while (true){
            String lockValue= UUID.randomUUID().toString();
            if (jedisCluster.setnx(lockKey, lockValue)==1){//key不存在，设值成功并返回1
                jedisCluster.expire(lockKey,locked_time);
                continue;
            }

            if (System.currentTimeMillis()<=endTime){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                throw new RuntimeException("key:"+key+" 等待超时，分布式锁获取失败");
            }
        }
    }

    /**
     * 悲观锁解锁
     * @param key 解锁键
     */
    public void unlock(String key){
        String lockKey=lock_prefix + ":" + key;
        jedisCluster.del(lockKey);
    }
}
