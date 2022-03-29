package com.atguigu.part02;

import com.atguigu.util.SleepUtils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lucky845
 * @date 2022年03月29日 16:16
 */
public class Test03_ReentrantLock {

    // 可重入锁
    private Lock lock = new ReentrantLock();
    // 通知队列
    private Condition condition = lock.newCondition();

    public static void main(String[] args) {
        final Test03_ReentrantLock testLock = new Test03_ReentrantLock();
        new Thread(() -> {
            try {
                testLock.method1();
            } catch (Exception ignored) {
            }
        }, "AA").start();
        SleepUtils.second(1);
        new Thread(testLock::method2, "BB").start();
    }

    public void method1() throws Exception {
        lock.lock();
        System.out.println("当前线程" + Thread.currentThread().getName() + "进入等待状态1");
        SleepUtils.second(3);
        System.out.println("当前线程" + Thread.currentThread().getName() + "释放锁2");
        condition.await();// Object
        System.out.println("当前线程" + Thread.currentThread().getName() + "继续执行3");
        lock.unlock();
    }

    public void method2() {
        lock.lock();
        System.out.println("当前线程" + Thread.currentThread().getName() + "进入等待状态4...");
        SleepUtils.second(3);
        System.out.println("当前线程" + Thread.currentThread().getName() + "发出唤醒5...");
        condition.signal();
        lock.unlock();
    }

}
