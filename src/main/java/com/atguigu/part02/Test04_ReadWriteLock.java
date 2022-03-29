package com.atguigu.part02;

import com.atguigu.util.SleepUtils;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lucky845
 * @date 2022年03月29日 16:23
 */
public class Test04_ReadWriteLock {

    /*
        读写锁
            读读共享，读写互斥，写写互斥
            读写锁是一种特殊的锁，适用于高并发情况下，读多写少的情况
     */

    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    public static void main(String[] args) {

        final Test04_ReadWriteLock rwl = new Test04_ReadWriteLock();

        // 读线程
        Thread t1 = new Thread(rwl::read, "t1");
        Thread t2 = new Thread(rwl::read, "t2");

        // 写线程
        Thread t3 = new Thread(rwl::write, "t3");
        Thread t4 = new Thread(rwl::write, "t4");

//        t1.start();//R
//        t2.start();//R

        t1.start(); // R
        t3.start(); // W

//        t3.start();// W
//        t4.start();// W
    }

    public void read() {
        readLock.lock();
        String threadName = Thread.currentThread().getName();
        System.out.println("当前线程:" + threadName + "读进入...");
        SleepUtils.second(3);
        System.out.println("当前线程:" + threadName + "读退出...");
        readLock.unlock();
    }

    public void write() {
        writeLock.lock();
        String name = Thread.currentThread().getName();
        System.out.println("当前线程:" + name + "写进入...");
        SleepUtils.second(3);
        System.out.println("当前线程:" + name + "写退出...");
        writeLock.unlock();
    }

}
