package com.atguigu.part03;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author lucky845
 * @date 2022年03月30日 16:09
 */
public class Test01_ThreadPool {

    public static void main(String[] args) {

        // 1. 创建一个固定数量的线程池，适用于执行长期任务
        ExecutorService threadPool1 = Executors.newFixedThreadPool(3);
        // 2. 创建一个单个线程的线程池，任何时候都只有一个线程在执行，能够保证任务具备一定的顺序
        ExecutorService threadPool2 = Executors.newSingleThreadExecutor();
        // 3. 创建一个可扩展的线程池，适合于短时异步任务
        ExecutorService threadPool3 = Executors.newCachedThreadPool();
        // 4. 创建一个可以定时操作的线程池，定时任务
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);

        for (int i = 1; i <= 100; i++) {
            int a = i;
            threadPool.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "银行柜台为第" + a + "位用户服务");
            });
        }
        threadPool.shutdown();

    }

}
