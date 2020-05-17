package com.learn.demo.thread;

class DeadLockRunnable implements Runnable{

    private String lockA;
    private String lockB;

    public DeadLockRunnable(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName()+"\t持有"+lockA);
            synchronized (lockB){
                System.out.println(Thread.currentThread().getName()+"\t尝试获取"+lockB);
            }
        }
    }
}

public class DeadLockDemo {
    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";
        new Thread(new DeadLockRunnable(lockA,lockB),"AAA").start();
        new Thread(new DeadLockRunnable(lockB,lockA),"BBB").start();
    }
}
