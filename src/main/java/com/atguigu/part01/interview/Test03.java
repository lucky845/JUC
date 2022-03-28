package com.atguigu.part01.interview;

/**
 * @author lucky845
 * @date 2022年03月28日 22:11
 */
public class Test03 {

    /*
         线程1打印：ABCDE,线程2打印：12345。 两个线程交叉输出打印。
     */

    public static void main(String[] args) {

        char[] chars = new char[26];
        Object o = new Object();

        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ('A' + i);
        }

        new Thread(() -> {
            synchronized (o) {
                for (int i = 1; i <= chars.length; i++) {
                    System.out.println(i);
                    try {
                        o.notify();
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        }, "线程1").start();

        new Thread(() -> {
            synchronized (o) {
                for (char ch : chars) {
                    System.out.println(ch);
                    try {
                        o.notify();
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        }, "线程2").start();

    }

}
