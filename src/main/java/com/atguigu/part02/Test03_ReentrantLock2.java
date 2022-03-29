package com.atguigu.part02;

import com.atguigu.util.SleepUtils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lucky845
 * @date 2022年03月29日 16:21
 */
public class Test03_ReentrantLock2 {

    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();

    public static void main(String[] args) {

        final Test03_ReentrantLock2 umc = new Test03_ReentrantLock2();
        Thread t1 = new Thread(umc::m1, "t1");
        Thread t2 = new Thread(umc::m2, "t2");
        Thread t3 = new Thread(umc::m3, "t3");
        Thread t4 = new Thread(umc::m4, "t4");
        Thread t5 = new Thread(umc::m5, "t5");

        t1.start();    // c1  m1 await
        t2.start();    // c1  m2 await
        t3.start();    // c2  m3 await

        SleepUtils.second(2);

        t4.start();    // c1  singalAll

        SleepUtils.second(2);

        t5.start();    // c2 singal

    }

    public void m1() {

        try {
            lock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "进入方法m1等待..");
            c1.await();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "方法m1继续..");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void m2() {
        try {
            lock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "进入方法m2等待..");
            c1.await();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "方法m2继续..");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void m3() {
        try {
            lock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "进入方法m3等待..");
            c2.await();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "方法m3继续..");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void m4() {
        try {
            lock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "唤醒..");
            c1.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void m5() {
        try {
            lock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "唤醒..");
            c2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}