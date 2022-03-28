package com.atguigu.part01;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lucky845
 * @date 2022年03月28日 20:32
 */
public class Test11_NotSafe {

    public static void main(String[] args) {
        // 线程不安全的map
//        Map<String, String> map = new HashMap<>();
        // 线程安全的map 效率低
//        Map<String, String> map = new Hashtable<>();
        // 线程安全的 效率高
        Map<String, String> map = new ConcurrentHashMap<>();

        for (int i = 0; i < 100; i++) {
            String key = "" + i;
            new Thread(() -> {
                map.put(key, UUID.randomUUID().toString().substring(0, 6));
                System.out.println(map);
            }).start();
        }
    }

}
