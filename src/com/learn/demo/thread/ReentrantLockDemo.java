package com.learn.demo.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareResources {
    private int number = 1;//A1;B2;c3
    private Lock lock = new ReentrantLock();
    private Condition a1 = lock.newCondition();
    private Condition b2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    void print1(){
        try {
            lock.lock();
            while (number != 1){
                a1.await();
            }
            //A开始干活
            for (int i = 0; i < 1; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            //修改标志位
            number = 2;
            //通知B
            b2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    void print2(){
        try {
            lock.lock();
            while (number != 2){
                b2.await();
            }
            //B开始干活
            for (int i = 0; i < 2; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            //修改标志位
            number = 3;
            //通知C
            c3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    void print3(){
        try {
            lock.lock();
            while (number != 3){
                c3.await();
            }
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            //修改标志位
            number = 1;
            //通知A
            a1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

public class ReentrantLockDemo {
    public static void main(String[] args) {
        ShareResources shareResources = new ShareResources();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResources.print1();
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResources.print2();
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResources.print3();
            }
        }, "C").start();
    }
}
