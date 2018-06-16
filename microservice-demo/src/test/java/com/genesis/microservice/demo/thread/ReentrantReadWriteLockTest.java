package com.genesis.microservice.demo.thread;

import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Aizhanglin on 2018/3/3.
 */
public class ReentrantReadWriteLockTest {
    @Test
    public void test(){
        CountDownLatch countDownLatch=new CountDownLatch(100);
        Cache cache=new Cache();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                Integer value=cache.get("aa");
                System.out.println(value);
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("结束");
    }
    class Cache{
        private AtomicInteger atomicInteger=new AtomicInteger(0);
        private Map<String,Integer> datas=new HashMap();

        ReadWriteLock readWriteLock=new ReentrantReadWriteLock();
        public Integer get(String key){
            Integer value;
            try {
                readWriteLock.readLock().lock();
                value = datas.get(key);
                if (StringUtils.isEmpty(value)){
                    try {
                        readWriteLock.readLock().unlock();
                        readWriteLock.writeLock().lock();
                        value=datas.get(key);
                        if (StringUtils.isEmpty(value)){
                            value=atomicInteger.incrementAndGet();
                            System.out.println("设置值");
                            datas.put(key,value);
                        }
                        readWriteLock.readLock().lock();
                    }finally {
                        readWriteLock.writeLock().unlock();
                    }
                }

            }finally {
                readWriteLock.readLock().unlock();
            }
            return value;
        }
    }
}
