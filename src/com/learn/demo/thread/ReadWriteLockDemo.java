package com.learn.demo.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class CacheDemo {
    //内存资源类需要保证可见性
    private volatile Map<String, String> map = new HashMap<>();
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    void beforeLockPut(String key, String value) {
        System.out.println(Thread.currentThread().getName() + "\t正在写操作");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        map.put(key, value);
        System.out.println(Thread.currentThread().getName() + "\t写入完成，值为：" + value);
    }

    void beforeLockGet(String key) {
        System.out.println(Thread.currentThread().getName() + "\t正在读操作");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = map.get(key);
        System.out.println(Thread.currentThread().getName() + "\t读取完成,值为：" + result);
    }

    void Put(String key, String value) {
        try {
            readWriteLock.writeLock().lock();
            System.out.println(Thread.currentThread().getName() + "\t正在写操作");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "\t写入完成，值为：" + value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    void Get(String key) {
        try {
            readWriteLock.readLock().lock();
            System.out.println(Thread.currentThread().getName() + "\t正在读操作");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String result = map.get(key);
            System.out.println(Thread.currentThread().getName() + "\t读取完成,值为：" + result);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        System.out.println("====================加读写锁之前=======================");
        CacheDemo cacheDemo = new CacheDemo();
        for (int i = 0; i < 5; i++) {
            final int tempI = i;
            new Thread(() -> {
                cacheDemo.beforeLockPut("key" + tempI, "value" + tempI);
            }, "写入线程T" + i).start();
        }
        for (int i = 0; i < 5; i++) {
            final int tempI = i;
            new Thread(() -> {
                cacheDemo.beforeLockGet("key" + tempI);
            }, "读取线程T" + i).start();
        }
        try {
            Thread.sleep(5*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("====================加读写锁之后=======================");
        for (int i = 0; i < 5; i++) {
            final int tempI = i;
            new Thread(() -> {
                cacheDemo.Put("key" + tempI, "value" + tempI);
            }, "写入线程T" + i).start();
        }
        for (int i = 0; i < 5; i++) {
            final int tempI = i;
            new Thread(() -> {
                cacheDemo.Get("key" + tempI);
            }, "读取线程T" + i).start();
        }
    }
}
