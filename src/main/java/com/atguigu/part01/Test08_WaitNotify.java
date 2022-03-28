package com.atguigu.part01;

import com.atguigu.util.SleepUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author lucky845
 * @date 2022年03月28日 19:56
 */
public class Test08_WaitNotify {

    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) throws Exception {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();

        TimeUnit.SECONDS.sleep(1);

        Thread notifyThread = new Thread(new Notify(), "NotifyThread");
        notifyThread.start();
    }

    static class Wait implements Runnable {
        public void run() {
            synchronized (lock) {
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread() + "wait线程位置1");
                        // 释放锁
                        lock.wait();
                    } catch (InterruptedException e) {
                    }
                }
                // 条件满足时，完成工作
                System.out.println(Thread.currentThread() + "wait线程位置2 有可能最后执行");
            }
        }
    }

    static class Notify implements Runnable {
        public void run() {
            synchronized (lock) {
                System.out.println(Thread.currentThread() + "Notify线程位置1");
                // 不是马上释放该线程的执行权限 也不释放锁 通知wait的线程
                lock.notifyAll();
                flag = false;
                SleepUtils.second(5);
            }
            // 再次加锁
            synchronized (lock) {
                System.out.println(Thread.currentThread() + "Notify线程位置2再次加锁");
                SleepUtils.second(5);
            }
        }
    }

}
