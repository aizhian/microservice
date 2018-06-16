package com.genesis.microservice.demo.thread;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by Aizhanglin on 2018/3/17.
 */
public class AtomicIntegerArrayTest {
    AtomicIntegerArray aia=new AtomicIntegerArray(new int[]{1,2,3});
    @Test
    public void foo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    aia.getAndAdd(0,1);
                    System.out.println("thread 1："+aia.get(0));
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    aia.getAndAdd(0,1);
                    System.out.println("thread 2："+aia.get(0));
                }
            }
        }).start();
    }
}
