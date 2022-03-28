package com.atguigu.part01;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author lucky845
 * @date 2022年03月28日 20:29
 */
public class Test10_NotSafe {

    public static void main(String[] args) {
        // 线程不安全的集合
//        Set<String> set = new HashSet<>();
        // 线程不安全的集合
//        Set<String> set = new TreeSet<>();
        // 线程安全
        Set<String> set = new CopyOnWriteArraySet<>();

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 6));
                System.out.println(set);
            }).start();
        }
    }

}
