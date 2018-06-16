package com.genesis.microservice.demo.thread;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Aizhanglin on 2018/3/14.
 */
public class ThreadGloablePropertyTest {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (; ; ) {
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;

            Thread one = new Thread(() -> {
                a =1 ;
                x = b;
            });

            Thread other = new Thread(() -> {
                y = a;
                b =1;
            });
            one.start();
            other.start();
            one.join();
            other.join();

            String result = "第" + i + "次 (" + x + "," + y + "）";
            if (x == 0 && y == 0) {
                System.err.println(result);
                break;
            } else {
                System.out.println(result);
            }
        }
    }

}
