package com.learn.demo.thread;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 演示ABA问题的产生及解决办法
 */
public class ABADemo {
    public static void main(String[] args) {
        System.out.println("===================ABA问题的产生===================");
        AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
        new Thread(() -> {
            System.out.println(atomicReference.compareAndSet(100, 101) + "\t" + Thread.currentThread().getName() + "\t修改当前的atomicInteger值为："+atomicReference.get());
            System.out.println(atomicReference.compareAndSet(101, 100) + "\t" + Thread.currentThread().getName() + "\t修改当前的atomicInteger值为："+atomicReference.get());
        },"T1").start();
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName()+"\t线程等待1秒钟，等上一个线程完成ABA操作");
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(atomicReference.compareAndSet(100, 2020) + "\t" + Thread.currentThread().getName()+"\t修改当前atomicInteger的值为："+atomicReference.get());
        },"T2").start();
        try {
            //线程等待2秒钟，等待走完ABA问题的产生过程
            Thread.sleep(2*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("===================ABA问题的解决===================");
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t获取当前atomicStampedReference的时间戳版本号:"+atomicStampedReference.getStamp());
            try {
                System.out.println(Thread.currentThread().getName()+"\t线程等待1秒钟，确保下面的线程获取同一个版本号");
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(atomicStampedReference.compareAndSet(100, 101, 1, atomicStampedReference.getStamp()+1)
                    + "\t" + Thread.currentThread().getName()+"\t当前atomicStampedReference的值为："+atomicStampedReference.getReference()+"\t当前版本号为:"+atomicStampedReference.getStamp());
            System.out.println(atomicStampedReference.compareAndSet(101, 100, 2, atomicStampedReference.getStamp()+1)
                    + "\t" + Thread.currentThread().getName()+"\t当前atomicStampedReference的值为："+atomicStampedReference.getReference()+"\t当前版本号为:"+atomicStampedReference.getStamp());
        },"T3").start();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t获取当前atomicStampedReference的时间戳版本号:"+atomicStampedReference.getStamp());
            try {
                System.out.println(Thread.currentThread().getName()+"\t线程等待3秒钟，等上一个线程完成ABA操作");
                Thread.sleep(3*1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //期望版本号没有被改动过
            System.out.println(atomicStampedReference.compareAndSet(101, 2020, 1, atomicStampedReference.getStamp()+1)
                    + "\t" + Thread.currentThread().getName()+"\t当前atomicStampedReference的值为："+atomicStampedReference.getReference()+"\t当前版本号为:"+atomicStampedReference.getStamp());
        },"T4").start();
    }
}
