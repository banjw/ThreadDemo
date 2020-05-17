package com.learn.demo.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println("================召唤神龙================");
        });
        for (int i = 0; i < 7; i++) {
            int tempI = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+"正在收集到的龙珠编号为："+tempI);
                try {
                    Thread.sleep(5*1000L);
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"收集到的龙珠编号为："+tempI);
            }, "T"+i).start();
        }
    }
}
