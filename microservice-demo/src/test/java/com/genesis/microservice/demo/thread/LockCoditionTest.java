package com.genesis.microservice.demo.thread;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Aizhanglin on 2018/3/22.
 * 使用lock condition实现先进先出队列
 */
public class LockCoditionTest {
    @Test
    public void foo(){
        Queue queue=new Queue();
        for (int i = 0; i < 15; i++) {
            new Thread(() -> {
                try {
                    queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    queue.put(finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        while (true){}
    }

    class Queue{
        Lock lock=new ReentrantLock();
        Condition notEmpt=lock.newCondition();
        Condition notFull=lock.newCondition();
        Object[] data=new Object[10];
        int putPst,tackPst,count;
        public void put(Object object) throws InterruptedException {
            lock.lock();
            try {
                while (count==data.length){
                    notFull.await();
                }
                data[putPst]=object;
                if (++putPst==data.length){
                    putPst=0;
                }
                count++;
                notEmpt.signal();
                System.out.println(object+" 进去了");
            }finally {
                lock.unlock();
            }
        }
        public Object take() throws InterruptedException {
            lock.lock();
            try {
                while (count==0){
                    notEmpt.await();
                }
                Object object = this.data[tackPst];
                if (++tackPst==data.length){
                    tackPst=0;
                }
                notFull.signal();
                count--;
                System.out.println(object+" 出来了");
                return object;
            }finally {
                lock.unlock();
            }
        }
    }
}
