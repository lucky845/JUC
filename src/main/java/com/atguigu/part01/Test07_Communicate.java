package com.atguigu.part01;

/**
 * @author lucky845
 * @date 2022年03月28日 19:04
 */
public class Test07_Communicate {

    /*
        线程通信
            题目： 一个线程进行加法 0才能加，另一个线程减法，1才能减
     */

    public static void main(String[] args) {
        Business business = new Business();

        new Thread(incr(business), "父线程1").start();
        new Thread(decr(business), "子线程1").start();
        // =======================================
        new Thread(incr(business), "父线程2").start();
        new Thread(decr(business), "子线程2").start();

    }

    /**
     * 加一的线程
     */
    private static Runnable decr(Business business) {
        return () -> {
            try {
                for (int i = 0; i < 6; i++) {
                    business.decr();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    /**
     * 减一的线程
     */
    private static Runnable incr(Business business) {
        return () -> {
            try {
                for (int i = 0; i < 6; i++) {
                    business.incr();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}

class Business {

    private int num = 0;

    /**
     * 加一的方法
     */
    public synchronized void incr() throws Exception {
        // 判断num当前值是否为0
        while (num != 0) {
            wait();
        }
        System.out.println(Thread.currentThread().getName() + "\t" + num);
        // 对num进行+1
        num++;
        // 唤醒另一个线程
        notifyAll();
    }

    /**
     * 减一的方法
     */
    public synchronized void decr() throws Exception {
        // 判断num当前值是否为1
        while (num != 1) {
            wait();
        }
        System.out.println(Thread.currentThread().getName() + "\t" + num);
        // 对num进行-1
        num--;
        // 唤醒另一个线程
        notifyAll();
    }
}