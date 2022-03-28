package com.atguigu.part01;

import com.atguigu.util.SleepUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lucky845
 * @date 2022年03月28日 18:08
 */
public class Test04_ThreadState {

    /*
        线程的状态
            a. 查看线程的状态
                1. 使用jps查看线程的线程号，然后使用jstack查看对应线程号的某个时刻的运行状态（线程的快照）
                2. 使用jvisualvm-对线程进行dump
            b. 线程调用sleep()
                进入TimeWaiting状态
            c. 线程调用wait()
                进入Waiting状态
            d. 当一个线程进入，另一个线程已经拿到锁（synchronized）
                进入Blocked（阻塞）状态
            e. 当一个线程进入，另一个线程已经拿到锁（lock）
                进入Waiting状态
            f. synchronized 与 lock 的区别
                ● Lock锁更加的面向对象
                ● 两个锁加锁后另一个线程进入的状态不同
                ● synchronized进入blocked状态是被动地 还没有进入到同步代码块内
                ● Lock锁进入Waiting主动的，已经进入到代码块里面(程序恢复执行之后 它会从刚刚暂停的地方恢复回来)
                ● synchronized 缺点 就算是去读也会进行加锁
                ● lock 可以主动释放锁 能做synchronized做的所有事情，更加面向对象
                ● jdk1.6以后对synchronized进行了优化 所以性能相差不大
     */

    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {

        // 一个线程调用 sleep() 进入 TimeWaiting 状态， 另一个线程 调用 wait() 进入 waiting 状态
//        new Thread(new TimeWaiting(), "TimeWaitingThread").start();
//        new Thread(new Waiting(), "WaitingThread").start();

        // 使用两个Blocked线程，一个获取锁成功，另一个被阻塞
//        new Thread(new Blocked(), "BlockedThread-1").start();
//        new Thread(new Blocked(), "BlockedThread-2").start();

        // 使用两个Sync线程，一个获取锁成功，另一个 waiting 状态
        new Thread(new Sync(), "SyncThread-1").start();
        new Thread(new Sync(), "SyncThread-2").start();
    }

    /**
     * 该线程不断的进行睡眠
     */
    static class TimeWaiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                SleepUtils.second(100);
            }
        }
    }

    /**
     * 该线程在Waiting.class实例上等待
     */
    static class Waiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class) {
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 该线程在Blocked.class实例上加锁后，不会释放该锁
     */
    static class Blocked implements Runnable {
        public void run() {
            synchronized (Blocked.class) {
                while (true) {
                    SleepUtils.second(100);
                }
            }
        }
    }

    /**
     * 该线程使用lock加锁后，先TimeWaiting状态然后释放锁
     */
    static class Sync implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                SleepUtils.second(100);
            } finally {
                lock.unlock();
            }
        }

    }

}
