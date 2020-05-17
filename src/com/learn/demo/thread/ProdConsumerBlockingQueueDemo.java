package com.learn.demo.thread;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class ResourcesData{
    private volatile boolean flag = true;
    private AtomicInteger atomicInteger = new AtomicInteger();
    private BlockingQueue<String> blockingQueue;

    ResourcesData(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    void product(){
        try {
            String data;
            boolean offerFlag;
            while (flag){
                data = atomicInteger.getAndIncrement()+"";
                offerFlag = blockingQueue.offer(data, 2L, TimeUnit.SECONDS);
                if(offerFlag){
                    System.out.println(Thread.currentThread().getName()+"\t往队列中添加数据成功，数据为："+data);
                }else {
                    System.out.println(Thread.currentThread().getName()+"\t往队列中添加数据失败，数据为："+data);
                }
                Thread.sleep(1000L);
            }
            System.out.println(Thread.currentThread().getName()+"\t总开关已经关闭，生产结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void consumer(){
        try {
            String result;
            while (flag){
                result = blockingQueue.poll(2L, TimeUnit.SECONDS);
                if(null == result || "".equals(result)){
                    System.out.println(Thread.currentThread().getName()+"\t从队列中获取数据失败");
                    break;
                }
                System.out.println(Thread.currentThread().getName()+"\t从队列中获取数据成功，数据为："+result);
//                Thread.sleep(1000L);
            }
            System.out.println(Thread.currentThread().getName()+"\t总开关已经关闭，结束消费");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void stop(){
        this.flag = false;
    }
}

public class ProdConsumerBlockingQueueDemo {
    public static void main(String[] args) {
        ResourcesData resourcesData = new ResourcesData(new ArrayBlockingQueue<>(10));
        new Thread(() -> {
            resourcesData.product();
        }, "product").start();

        new Thread(() -> {
            resourcesData.consumer();
        }, "consumer").start();
        try {
            Thread.sleep(10*1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"\t调用停止方法");
        resourcesData.stop();
    }
}
