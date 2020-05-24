package com.learn.demo.thread;


import java.util.concurrent.atomic.AtomicInteger;

class Person{
    volatile int age = 0;
    void addAgeTo28(){
        this.age = 28;
    }
    //请注意，现在的age是有volatile修饰的，但是volatile不保证原子性
    void pulsAge(){
        age++;
    }
    //如何解决原子性？1.加sync；2.使用atomic包地下的类
    AtomicInteger atomicInteger = new AtomicInteger();
    void getAndInAtomicInteger(){
        atomicInteger.getAndIncrement();
    }
}

public class VolatileDemo {
    public static void main(String[] args) {
//        showVolatileVisibility();
        notSupportAtomic();
    }

    /**
     * volatile的原子性不可保证，即保证不了数据完整性
     */
    private static void notSupportAtomic() {
        for (int k = 0; k < 10 ; k++) {
            Person person = new Person();
            for (int i = 1; i <=20 ; i++) {
                new Thread(() -> {
                    for (int j = 1; j <= 1000 ; j++) {
                        person.pulsAge();
                        person.getAndInAtomicInteger();
                    }
                }).start();
            }

            while (Thread.activeCount() > 2){
                Thread.yield();
            }
            System.out.println(Thread.currentThread().getName()+",int type,最终age的值为:"+person.age);
            System.out.println(Thread.currentThread().getName()+",AtomicInteger type,最终age的值为:"+person.atomicInteger);
        }
    }

    /**
     * volatile的可见性，当有线程修改了主内存中的值的时候，及时通知其他线程
     */
    private static void showVolatileVisibility() {
        Person person = new Person();
        new Thread(() -> {
            System.out.println("进入线程AAA中。。。");
            try{
                Thread.sleep(3*1000L);
            }catch (Exception e){
                e.printStackTrace();
            }
            person.addAgeTo28();
            System.out.println(Thread.currentThread().getName()+"线程修改了age的值，现在age的值为:"+person.age);
        },"AAA").start();

        while (person.age == 0){

        }
        System.out.println(Thread.currentThread().getName()+"主线程已经结束，age的值为:"+person.age);
    }
}
