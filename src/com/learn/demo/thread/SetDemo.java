package com.learn.demo.thread;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class SetDemo {
    /**
     * 1.产生异常
     *      java.util.ConcurrentModificationException
     *
     * 2.产生原因
     *      多线程环境下，并发资源抢夺修改导致的数据不一致异常
     *
     * 3.解决办法
     *      3.1 Collections.synchronizedSet(new HashSet<>())
     *      3.2 CopyOnWriteArraySet
     *
     * 4.优化建议
     *      写时复制
     *         CopyOnWrite容器即写时复制的容器，往一个容器添加元素的时候，不直接往当前容器Object[]添加，而是先将当前容器Object[]进行copy，复制出一个新的容器Object[] newElements，
     *         然后往新的容器Object[] newElements 里添加元素，添加完成之后，再将原容器的引用指向新的容器setArray(newElements)；这样做的好处是可以对容器进行并发的读，而不需要加锁，
     *         因为当前容器不会添加任何元素，所以CopyOnWrite容器也是一种读写分离的思想
     *
     */

    public static void main(String[] args) {
        //其底层就是CopyOnWriteArrayList
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i <30 ; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
        //HashSet的底层实现，是hashMap，因为HashSet只关心key，不关心value（固定常量类private static final Object PRESENT = new Object()）
        new HashSet<>();
    }

    private static void synchronizedSetSafeDemo() {
        Set<String> set = Collections.synchronizedSet(new HashSet<>());
        for (int i = 0; i <30 ; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }

    private static void hashSetNotSafe() {
        Set<String> set = new HashSet<>();
        for (int i = 0; i <30 ; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
        //上面代码会产生java.util.ConcurrentModificationException
    }
}
