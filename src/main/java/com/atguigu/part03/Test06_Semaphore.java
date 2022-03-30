package com.atguigu.part03;

import com.atguigu.util.SleepUtils;

import java.util.concurrent.Semaphore;

/**
 * @author lucky845
 * @date 2022年03月30日 18:14
 */
public class Test06_Semaphore {

    public static void main(String[] args) {

        /*
            Semaphore: 信号量
                资源有限，大家要抢夺资源，只用一会
         */

        Semaphore semaphore = new Semaphore(3);

        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {

                try {
                    // 获取资源
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "号车进入停车位");
                    SleepUtils.second(2);
                    System.out.println(Thread.currentThread().getName() + "号车离开停车位");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 释放资源
                    semaphore.release();
                }

            }, String.valueOf(i)).start();
        }

    }

}
