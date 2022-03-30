package com.atguigu.part03;

import com.atguigu.util.SleepUtils;

import java.util.concurrent.CountDownLatch;

/**
 * @author lucky845
 * @date 2022年03月30日 18:09
 */
public class Test05_CountDownLatch {

    public static void main(String[] args) throws Exception {

        /*
            CountDownLatch: 一个程序需要等待程序做完某些事情之后才能做某件事情
         */

        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            SleepUtils.second(2);
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "号同学离开教室");
                // 计数器减一
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }
        // 等待计数器清零
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "班长离开教师");

    }

}
