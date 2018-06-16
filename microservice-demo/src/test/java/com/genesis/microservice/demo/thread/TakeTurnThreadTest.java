package com.genesis.microservice.demo.thread;


import org.junit.Test;

/**
 *  a线程打印1次后 b线程打印2次，如此重复50次
 * Created by Aizhanglin on 2018/2/23.
 */
public class TakeTurnThreadTest {

    @Test
    public void tt() {
        Stage stage = new Stage();
        for (int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    stage.sub1();
                }
            }).start();
        }
        for (int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        stage.sub2();
                    }catch (Exception e){
                        System.out.println("interrupted");
                    }
                }
            }).start();
        }
        while (true){
        }
    }
    public class Stage{
        boolean sub1Flag;
        public synchronized void sub1(){
            while (!sub1Flag){
                try {
                    this.wait();
                    System.out.println(Thread.currentThread().getName()+" sub1 has awake");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < 1; i++) {
                System.out.println("sub1:"+i);
            }
            sub1Flag=false;
            this.notify();
        }

        public synchronized void sub2(){
            while (sub1Flag){
                try {
                    this.wait();
                    System.out.println(Thread.currentThread().getName()+" sub2 has awake");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < 2; i++) {
                System.out.println("sub2:"+i);
            }
            sub1Flag=true;
            this.notify();
        }
    }
}
