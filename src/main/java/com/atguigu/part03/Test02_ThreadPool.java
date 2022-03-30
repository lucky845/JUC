package com.atguigu.part03;

import com.atguigu.util.SleepUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lucky845
 * @date 2022年03月30日 16:20
 */
public class Test02_ThreadPool {

    public static void main(String[] args) {

        /**
         * @param corePoolSize 核心线程数
         * @param maximumPoolSize 最大线程数
         * @param keepAliveTime 存活时间
         * @param unit 时间单位
         * @param workQueue 阻塞队列
         * @param handler 拒绝策略
         */
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,
                5,
                3,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        /**
         * 1. 办理业务的人<=5,开启两个线程
         * 2. 办理业务的人<=8,开启<=5个线程
         * 3. 办理业务的人>8,产生拒绝策略
         *      默认的拒绝策略是： AbortPolicy() 直接抛出异常
         *                      DiscardPolicy() 丢弃新任务
         *                      DiscardOldestPolicy() 丢弃老任务，抛弃在队列中等待时间较长的老任务，适用于时效性较高的场景
         *                      CallerRunsPolicy()   任务来自于哪里，就交给哪里，能够最大限度的处理业务
         */
        for (int i = 1; i <= 10; i++) {
            int a = i;
            threadPool.submit(() -> {
                SleepUtils.second(2);
                System.out.println(Thread.currentThread().getName() + "银行柜台为第" + a + "位用户服务");
            });
        }
        threadPool.shutdown();

    }

}
