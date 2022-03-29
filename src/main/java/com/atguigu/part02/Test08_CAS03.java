package com.atguigu.part02;

import com.atguigu.util.SleepUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author lucky845
 * @date 2022年03月29日 17:06
 */
public class Test08_CAS03 {

    /*
        2.CAS的原理
            CAS机制中使用了3个操作数:内存地址V，旧的预期值A，要修改的新值B
            当我们要去更新一个值的时候 会把旧的预期值拿到同内存地址的值进行比较
            如果相同才进行置换
            unsafe为我们提供了基于硬件级别的原子操作
        3.使用AtomicStampedReference加上一个版本号，防止ABA问题
     */

    private static AtomicInteger count = new AtomicInteger(0);

    private static AtomicStampedReference<Integer> version = new AtomicStampedReference<>(0, 1);

    public static void main(String[] args) {

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 同步代码块
                synchronized (Test08_CAS03.class) {
                    for (int j = 0; j < 100; j++) {
                        count.incrementAndGet();
                    }
                }
            }).start();
        }

        SleepUtils.second(2);

        System.out.println("count = " + count);
    }

}
