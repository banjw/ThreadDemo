package com.learn.demo.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareData{
    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    void increment () {
        try {
            lock.lock();
            while ( number != 0){
                //等待不能生产
                condition.await();
            }
            //开始
            number++;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            //通知唤醒
            condition.signal();
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }
    void decrement () {
        try {
            lock.lock();
            while ( number != 1){
                //等待不能生产
                condition.await();
            }
            //开始
            number--;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            //通知唤醒
            condition.signal();
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }
}

public class ProdConsumerTraditionalDemo {
    public static void main(String[] args) {
        ShareData shareData = new ShareData();
//        for (int i = 0; i < 5; i++) {
//            new Thread(shareData::increment, "A"+i).start();
//        }
//        for (int i = 0; i < 5; i++) {
//            new Thread(shareData::decrement, "B"+i).start();
//        }
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                shareData.increment();
            }
        }, "T1").start();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                shareData.decrement();
            }
        }, "T2").start();
    }
}
