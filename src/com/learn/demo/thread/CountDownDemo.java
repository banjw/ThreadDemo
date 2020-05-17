package com.learn.demo.thread;

import java.util.concurrent.CountDownLatch;

public class CountDownDemo {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+"\t同学离开教室");
                countDownLatch.countDown();
            }, "T"+i).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"\t班长锁门");
    }
}
