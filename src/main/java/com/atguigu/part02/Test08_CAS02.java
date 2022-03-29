package com.atguigu.part02;

import com.atguigu.util.SleepUtils;

/**
 * @author lucky845
 * @date 2022年03月29日 17:06
 */
public class Test08_CAS02 {

    /*
        1.synchronized性能问题
            没有得到锁资源的线程进入BLOCKED状态
            争夺到锁资源后恢复为RUNNABLE状态
            在这个过程中涉及到用户模式和内核模式的转换
     */

    private static int count = 0;

    public static void main(String[] args) {

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 同步代码块
                synchronized (Test08_CAS02.class) {
                    for (int j = 0; j < 100; j++) {
                        // 不符合原子性，有加一和赋值两个步骤
                        count++;
                    }
                }
            }).start();
        }

        SleepUtils.second(2);

        System.out.println("count = " + count);
    }

}
