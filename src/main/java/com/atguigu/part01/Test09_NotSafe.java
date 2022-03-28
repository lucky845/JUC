package com.atguigu.part01;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lucky845
 * @date 2022年03月28日 20:18
 */
public class Test09_NotSafe {

    public static void main(String[] args) {
        // 线程不安全的集合
//        List<String> list = new ArrayList<>();
        // 使用一个工具类对其进行修改 包装一层 synchronized 效率不高 72ms
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
        // 线程安全的 效率不高 悲观锁/独占锁 69ms
//        List<String> list = new Vector<>();
        // 线程安全 效率高的 62ms
        List<String> list = new CopyOnWriteArrayList<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 6));
                System.out.println(list);
            }).start();
        }
        long end = System.currentTimeMillis();
        System.out.println("执行的时间为：" + (end - start));
    }

}
