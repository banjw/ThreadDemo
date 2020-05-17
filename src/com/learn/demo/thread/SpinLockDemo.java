package com.learn.demo.thread;

import java.util.concurrent.atomic.AtomicReference;

public class SpinLockDemo {
    private static AtomicReference<Thread> atomicReference = new AtomicReference<>();
    static void inSpinLock(){
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName()+"\tinvoke inSpinLock");
        while (!atomicReference.compareAndSet(null, thread)){

        }
    }
    static void unSpinLock(){
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
        System.out.println(thread.getName()+"\tinvoke unSpinLock");
    }

    public static void main(String[] args) {
        new Thread(() -> {
            inSpinLock();
            try {
                Thread.sleep(5*1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            unSpinLock();
        },"T1").start();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            inSpinLock();
            unSpinLock();
        },"T2").start();
    }
}
