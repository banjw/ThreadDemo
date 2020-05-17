package com.learn.demo.thread;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListDemo {
    /**
     * 1.产生异常
     *      java.util.ConcurrentModificationException
     *
     * 2.产生原因
     *      多线程环境下，并发资源抢夺修改导致的数据不一致异常
     *
     * 3.解决办法
     *      3.1 Vector
     *      3.2 Collections.synchronizedList(new ArrayList<>())
     *      3.3 CopyOnWriteArrayList
     * 4.优化建议
     *      写时复制
     *         CopyOnWrite容器即写时复制的容器，往一个容器添加元素的时候，不直接往当前容器Object[]添加，而是先将当前容器Object[]进行copy，复制出一个新的容器Object[] newElements，
     *         然后往新的容器Object[] newElements 里添加元素，添加完成之后，再将原容器的引用指向新的容器setArray(newElements)；这样做的好处是可以对容器进行并发的读，而不需要加锁，
     *         因为当前容器不会添加任何元素，所以CopyOnWrite容器也是一种读写分离的思想
     *         final ReentrantLock lock = this.lock;
     *         lock.lock();
     *         try {
     *             Object[] elements = getArray();
     *             int len = elements.length;
     *             if (index > len || index < 0)
     *                 throw new IndexOutOfBoundsException("Index: "+index+
     *                                                     ", Size: "+len);
     *             Object[] newElements;
     *             int numMoved = len - index;
     *             if (numMoved == 0)
     *                 newElements = Arrays.copyOf(elements, len + 1);
     *             else {
     *                 newElements = new Object[len + 1];
     *                 System.arraycopy(elements, 0, newElements, 0, index);
     *                 System.arraycopy(elements, index, newElements, index + 1,
     *                                  numMoved);
     *             }
     *             newElements[index] = element;
     *             setArray(newElements);
     *         } finally {
     *             lock.unlock();
     *         }
     */

    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i <30 ; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }

    private static void synchronizedListSafeDemo() {
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i <30 ; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }

    private static void vectorSafeDemo() {
        List<String> list = new Vector<String>();
        for (int i = 0; i <30 ; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }

    private static void ArrayListNotSafe() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i <30 ; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
        //上面代码会产生java.util.ConcurrentModificationException
    }
}
