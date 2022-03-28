package com.atguigu.part01.interview;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lucky845
 * @date 2022年03月28日 20:36
 */
public class Test01 {

    /*
        一共有3个线程，两个子线程先后循环2次，接着主线程循环3次，
        接着又回到两个子线程先后循环2次，再回到主线程又循环3次，如此循环5次。
     */

    public static void main(String[] args) {

        Business business = new Business();

        // 整体循环5次
        for (int j = 0; j < 5; j++) {
            // 子线程1执行2次
            for (int i = 0; i < 2; i++) {
                business.child1();
            }
            // 子线程2执行2次
            for (int i = 0; i < 2; i++) {
                business.child2();
            }
            // 主线程1执行3次
            for (int i = 0; i < 3; i++) {
                business.main();
            }
        }

    }

}

/**
 * 业务类
 */
class Business {

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();
    Lock lock3 = new ReentrantLock();

    public void child1() {
        synchronized (lock1) {
            lock2.lock();
            System.out.println("子线程1执行");
            lock2.unlock();
        }
    }

    public void child2() {
        synchronized (lock2) {
            lock3.lock();
            System.out.println("子线程2执行");
            lock3.unlock();
        }
    }

    public void main() {
        synchronized (lock3) {
            lock1.lock();
            System.out.println("主线程执行");
            lock1.unlock();
        }
    }

}
