package com.learn.demo.thread;


import java.util.concurrent.*;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());//查看电脑或服务器的CPU是几核的
        ExecutorService executorService = new ThreadPoolExecutor(
                2,
                5,
                2L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        try {
            for (int i = 0; i < 12; i++) {//直接丢弃任务，不予任何处理也不抛出异常，乳沟允许任务丢失，这是最好的一种方案
                executorService.execute(() -> {
                    System.out.println(Thread.currentThread().getName()+"\t开始运行");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private static void discardOldestPolicyDemo() {
        ExecutorService executorService = new ThreadPoolExecutor(
                2,
                5,
                2L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        try {
            for (int i = 0; i < 11; i++) {//抛弃队列中等待最久的任务
                executorService.execute(() -> {
                    System.out.println(Thread.currentThread().getName()+"\t开始运行");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private static void callerRunsPolicyDemo() {
        ExecutorService executorService = new ThreadPoolExecutor(
                2,
                5,
                2L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        try {
            for (int i = 0; i < 10; i++) {//多余的会交给调用线程执行，这里是main线程
                executorService.execute(() -> {
                    System.out.println(Thread.currentThread().getName()+"\t开始运行");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private static void abortPolicyDemo() {
        ExecutorService executorService = new ThreadPoolExecutor(
                2,
                5,
                2L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        try {
            for (int i = 0; i < 9; i++) {//RejectedExecutionException
                executorService.execute(() -> {
                    System.out.println(Thread.currentThread().getName()+"\t开始运行");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

    }
}
