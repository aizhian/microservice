package com.genesis.microservice.demo.thread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *  volatile关键字作用是强制子线程内存变量同步覆盖主线程内存变量，但是覆盖过程有延迟
 * Created by Aizhanglin on 2018/2/27.
 */
public class VolatileTest {

    private volatile Integer count = 0;

    @Test
    public void test() throws InterruptedException {
        List<Thread> threads=new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new SynThread());
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println(count);

    }

    class SynThread implements Runnable {
        private Object lock = new Object();

        @Override
        public void run() {
            while (count < 1000) {
                synchronized (lock) {
                    count++;
                    try {
                        lock.wait(5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
