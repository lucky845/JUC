package com.atguigu.part01;

import com.atguigu.util.SleepUtils;

/**
 * @author lucky845
 * @date 2022年03月28日 18:44
 */
public class Test05_DeadLockDemo {

    /*
        线程的死锁
            双方各自持有对方需要的锁，不释放 双方都处于一种Blocked状态
     */

    /**
     * A锁
     */
    private static String A = "A";
    /**
     * B锁
     */
    private static String B = "B";

    public static void main(String[] args) {

        Thread threadA = new Thread(() -> {
            synchronized (A) {
                SleepUtils.second(5);
                synchronized (B) {
                    System.out.println("A");
                }
            }
        });

        Thread threadB = new Thread(() -> {
            synchronized (B) {
                synchronized (A) {
                    System.out.println("B");
                }
            }
        });

        threadA.start();
        threadB.start();
    }

}
