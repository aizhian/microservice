package com.genesis.microservice.demo.thread;

import org.junit.Test;

/**
 * Created by Aizhanglin on 2018/3/9.
 */
public class SynchronizedThisTest {
    class ThreadX implements Runnable{
        @Override
        public void run() {
            synchronized (this){
                for (int i = 0; i <10 ; i++) {
                    System.out.println(Thread.currentThread().getName()+i);
                }
            }
        }
    }
    @Test
    public void test(){
        ThreadX threadX = new ThreadX();
        new Thread(threadX,"a").start();
        new Thread(threadX,"b").start();
    }
}
