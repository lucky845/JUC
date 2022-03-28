package com.atguigu.part01;

/**
 * @author lucky845
 * @date 2022年03月28日 17:17
 */
public class Test02_Synchronized {

    /*
        1. 普通方法 synchronized 默认加锁对象是当前类的对象（this）
        2. 静态方法 synchronized 默认加锁对象是当前类的字节码对象（xxx.class）
        3. synchronized 同步代码块加锁对象是指定的对象
     */

    public static void main(String[] args) {
        init();
    }

    public static void init() {
        OutPut outPut = new OutPut();

        // 线程一
        new Thread(() -> {
            // CPU一直运行
            while (true) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outPut.print("lucky845");
            }
        }).start();

        // 线程二
        new Thread(() -> {
            // CPU一直运行
            while (true) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                OutPut.print2("online");
            }
        }).start();

    }

}

class OutPut {
    public void print(String name) {
        int length = name.length();
        //Object object = new Object();
        synchronized (OutPut.class) {
            for (int i = 0; i < length; i++) {
                System.out.print(name.charAt(i));
            }
            System.out.println();
        }
    }

    public static synchronized void print2(String name) {
        int length = name.length();
        for (int i = 0; i < length; i++) {
            System.out.print(name.charAt(i));
        }
        System.out.println();
    }
}
