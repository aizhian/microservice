package com.genesis.microservice.demo.thread;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Aizhanglin on 2018/3/22.
 * lock锁实现方法互斥
 */
public class LockTest {
    @Test
    public void foo() throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cyclicBarrier=new CyclicBarrier(20);
        Product product = new Product();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    product.put();
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            cyclicBarrier.await();
        }

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    product.take();
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
    class Product{
        boolean tackFlag=false;
        Lock lock=new ReentrantLock();
        Condition takeCdt=lock.newCondition();
        public void put(){
            lock.lock();
            System.out.println(Thread.currentThread().getName()+"进入put lock");
            try {
                while (tackFlag){
                    try {
                        takeCdt.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tackFlag=true;
                System.out.println(Thread.currentThread().getName()+"put");
                takeCdt.signal();
            }finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName()+"释放put lock");
            }
        }
        public void take(){
            lock.lock();
            System.out.println(Thread.currentThread().getName()+"进入take lock");
            try {
                while (!tackFlag){
                    try {
                        takeCdt.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tackFlag=false;
                System.out.println(Thread.currentThread().getName()+"take");
                takeCdt.signal();
            }finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName()+"释放take lock");
            }
        }
    }
}
