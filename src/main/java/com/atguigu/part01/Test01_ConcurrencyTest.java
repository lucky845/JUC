package com.atguigu.part01;

/**
 * @author lucky845
 * @date 2022年03月28日 17:09
 */
public class Test01_ConcurrencyTest {

    /**
     * 执行次数
     * 执行次数较小时，并发比单线程慢
     */
    private static final long count = 1000001L;

    public static void main(String[] args) throws InterruptedException {
        // 并发计算
        concurrency();
        // 单线程计算
        serial();
    }

    private static void concurrency() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(() -> {
            int a = 0;
            for (long i = 0; i < count; i++) {
                a += 5;
            }
            System.out.println(a);
        });
        thread.start();
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        thread.join();
        long time = System.currentTimeMillis() - start;
        System.out.println("concurrency :" + time + "ms, b=" + b);
    }

    private static void serial() {
        long start = System.currentTimeMillis();
        int a = 0;
        for (long i = 0; i < count; i++) {
            a += 5;
        }
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("serial: " + time + "ms, b=" + b + ", a=" + a);
    }

}
