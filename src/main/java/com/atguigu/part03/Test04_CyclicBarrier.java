package com.atguigu.part03;

import com.atguigu.util.SleepUtils;

import java.util.concurrent.CyclicBarrier;

/**
 * @author lucky845
 * @date 2022年03月30日 18:02
 */
public class Test04_CyclicBarrier {

    public static void main(String[] args) {

        /*
            CyclicBarrier： 所有资源到位后一起去做某件事，资源之间需要互相等待
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println("召唤神龙");
        });

        for (int i = 1; i <= 7; i++) {
            SleepUtils.second(2);
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "号龙珠收集完成");
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }

    }

}
