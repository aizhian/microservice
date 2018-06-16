package com.genesis.microservice.demo.thread;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Aizhanglin on 2018/3/12.
 */
public class CountDownLatchTest {
    class Fn{
        public void fn1(){
            System.out.println("进入fn1");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("退出fn1");
        }
        public void fn2(){
            System.out.println("进入fn2");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("退出fn2");
        }
        public void fn3(){
            System.out.println("进入fn3");
            System.out.println("退出fn3");
        }
    }



    @Test
    public void test(){
        CountDownLatch countDownLatch=new CountDownLatch(2);
        Fn fn=new Fn();
        new Thread(() -> {
            fn.fn1();
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            fn.fn2();
            countDownLatch.countDown();
        }).start();
        Thread thread3=new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fn.fn3();
        });
        thread3.start();
        try {
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
