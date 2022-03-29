package com.atguigu.part02;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 编写一个简易缓存系统
 *
 * @author lucky845
 * @date 2022年03月29日 16:28
 */
public class Test05_MyCache {

    // 读写锁
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    // 缓存
    private Map<String, Object> cache = new HashMap<>();

    public static void main(String[] args) {

        Test05_MyCache myCache = new Test05_MyCache();
        Object data1 = myCache.getData("lucky845");
        Object data2 = myCache.getData("lucky845");
        System.out.println("data1 = " + data1);
        System.out.println("data2 = " + data2);

    }

    public Object getData(String key) {

        Object value = null;

        try {
            // 读锁
            rwLock.readLock().lock();
            value = cache.get(key);
            rwLock.readLock().unlock();

            // 判断是否需要加锁
            if (value == null) {
                // 写锁
                rwLock.writeLock().lock();
                // 是否需要查询数据库
                if (value == null) {
                    // 写操作
                    value = "you are so intelligent";
                    cache.put(key, value);
                }
                rwLock.writeLock().unlock();
            }

            rwLock.readLock().lock();
        } finally {
            rwLock.readLock().unlock();
        }
        return value;
    }

}
