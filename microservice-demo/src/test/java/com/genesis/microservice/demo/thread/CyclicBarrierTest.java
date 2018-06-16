package com.genesis.microservice.demo.thread;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Aizhanglin on 2018/3/11.
 * bc线程同时执行
 */
public class CyclicBarrierTest {

    @Test
    public void threadTest() throws InterruptedException {
        final CyclicBarrier barrier1 = new CyclicBarrier(2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    barrier1.await();
                    System.out.println("Thread B");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    barrier1.await();
                    System.out.println("Thread C");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
