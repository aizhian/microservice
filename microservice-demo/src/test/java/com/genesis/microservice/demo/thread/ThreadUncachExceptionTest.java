package com.genesis.microservice.demo.thread;

import lombok.Data;
import lombok.ToString;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Aizhanglin on 2018/6/5.
 */
public class ThreadUncachExceptionTest {
    ExecutorService executor = Executors.newFixedThreadPool(3);
    CountDownLatch countDownLatch=new CountDownLatch(1);
    @Data
    @ToString
    class ClienId{
        public ClienId(String cliendId){
            this.cliendId=cliendId;
        }
        private String cliendId;
        private boolean dealed;
    }
    public class FooThread implements Runnable {
        private List<ClienId> list;
        public FooThread(List<ClienId> list) {
            System.out.println("新线程构造"+list);
            this.list = list;
        }

        @Override
        public void run() {
            Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    System.out.println("处理线程异常");
                    List<ClienId> clienIds=new ArrayList<>();
                    for (ClienId clienId : list) {
                        if (!clienId.isDealed()){
                            clienIds.add(clienId);
                        }
                    }
                    System.out.println("新起线程"+clienIds);
                    Thread thread=new Thread(new FooThread(clienIds));
                    executor.execute(thread);
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (i>0){
                    throw new RuntimeException();
                }
                list.get(i).setDealed(true);
            }
            countDownLatch.countDown();
        }
    }

    @Test
    public void foo() {
        for (int i = 0; i < 1; i++) {
            List<ClienId> cliendIds = Arrays.asList(new ClienId("1"),new ClienId("2"));
            Thread thread = new Thread(new FooThread(cliendIds));
            executor.execute(thread);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(cliendIds);
        }

    }
}
