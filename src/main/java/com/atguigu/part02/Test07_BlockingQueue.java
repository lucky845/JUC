package com.atguigu.part02;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author lucky845
 * @date 2022年03月29日 16:47
 */
public class Test07_BlockingQueue {

    /*
        阻塞队列
            1. ArrayBlockingQueue： 数组实现的有界阻塞，指定数组的长度
            2. LinkedBlockingQueue： 一个由链表结构组成的有界阻塞队列，最大长度为 Integer.MAX_VALUE
            3. SynchronousQueue： 一个不存储元素的阻塞队列，只是起到一个传递数据的作用，
                                  必须取和存数据的线程同时存在才能往里面放数据

        阻塞队列的原理(重点)
            借助重入锁(乐观锁)+线程通信
            ArrayBlockingQueue的put和take使用的是同一把锁 put和take不能同时操作
            LinkedBlockingQueue的put和take使用的是不同的锁 put和take能同时操作
     */

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue blockingQueue = new ArrayBlockingQueue(3);

        // 使用add()与remove()
//        System.out.println(blockingQueue.add("a"));
//        System.out.println(blockingQueue.add("b"));
//        System.out.println(blockingQueue.add("c"));
        // 如果队列已满，抛出异常
//        System.out.println(blockingQueue.add("d"));
//        System.out.println(blockingQueue.remove());
//        System.out.println(blockingQueue.remove());
//        System.out.println(blockingQueue.remove());
        // 如果队列为空，抛出异常
//        System.out.println(blockingQueue.remove());

        // 使用offer()和poll()
//        System.out.println(blockingQueue.offer("a"));
//        System.out.println(blockingQueue.offer("b"));
//        System.out.println(blockingQueue.offer("c"));
        // 如果队列已满，返回false
//        System.out.println(blockingQueue.offer("d"));
//        System.out.println(blockingQueue.remove());
//        System.out.println(blockingQueue.remove());
//        System.out.println(blockingQueue.remove());
        // 如果队列为空，返回null
//        System.out.println(blockingQueue.remove());

        // 使用put()和take()
//        blockingQueue.put("a");
//        blockingQueue.put("b");
//        blockingQueue.put("c");
        // 如果队列已满，阻塞队列
        // blockingQueue.put("d");
//        System.out.println(blockingQueue.take());
//        System.out.println(blockingQueue.take());
//        System.out.println(blockingQueue.take());
        // 如果队列为空，阻塞队列
        // System.out.println(blockingQueue.take());

        // 使用offer()和poll()并设置超时时间
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        //如果队列已满 返回false
        System.out.println(blockingQueue.offer("d", 3, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        //如果队列已空 返回null
        System.out.println(blockingQueue.poll(3, TimeUnit.SECONDS));

    }

}
