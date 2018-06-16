package com.genesis.microservice.demo.thread;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * Created by Aizhanglin on 2018/3/19.
 */
public class ThreadPoolTest {
    @Test
    public void foo1(){
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("name:"+ finalI);
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void foo2(){
        ExecutorService executor=Executors.newFixedThreadPool(3);
        for (int i = 0; i < 100; i++) {
            Integer n=i;
            Future<Integer> future = executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
//                    Thread.sleep(3000);
                    System.out.println("name:"+n);
                    return n;
                }
            });
//            try {
//                Integer integer = future.get();
//                System.out.println("name"+integer);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
        }
        executor.shutdownNow();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
