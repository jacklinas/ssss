package com.gith.main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedLockTest {

    private static Object obj = new Object();
    private static ReentrantLock reentrantLock = new ReentrantLock(true);
    private static Condition condition = reentrantLock.newCondition();

    public static void jdkLock(final int num){
        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println(num + "线程获取锁");
                reentrantLock.lock();

                if(num == 10){
                    System.out.println(num + "通知========================");
                    condition.signalAll();
                    System.out.println(num + "通知完成");
                    reentrantLock.unlock();
                    return;
                }

                System.out.println(num + "线程已枷锁");
                System.out.println(num + "线程等待条件");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(num + "线程已获取条件");

                System.out.println(num + "线程解锁");
                reentrantLock.unlock();
                System.out.println(num + "线程解锁完成");
            }
        }).start();
    }

    public static void syncLock(final int num){
        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println(num + "线程获取锁");
                synchronized (obj) {
                    if (num == 10) {
                        System.out.println(num + "通知========================");
                        obj.notify();
                        System.out.println(num + "通知完成");
                        return;
                    }

                    System.out.println(num + "线程已枷锁");
                    System.out.println(num + "线程等待条件");
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(num + "线程已获取条件");
                    System.out.println(num + "线程解锁完成即将推出");
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        for (int i=0; i <= 10 ; i++){
            syncLock(i);
        }
    }


}
