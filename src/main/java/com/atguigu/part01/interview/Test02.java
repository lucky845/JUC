package com.atguigu.part01.interview;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lucky845
 * @date 2022年03月28日 20:53
 */
public class Test02 {

    /*
          实现一个容器，提供两个方法，add，size 写两个线程，线程1添加10个元素到容器中，
          线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
     */

    public static void main(String[] args) {

        Container container = new Container();

        new Thread(() -> {
            synchronized (container) {
                for (int i = 1; i <= 10; i++) {
                    if (container.list.size() == 5) {
                        try {
                            container.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("i = " + i);
                    container.add(i);
                }
            }
        }, "线程1").start();

        new Thread(() -> {
            synchronized (container) {
                while (container.list.size() != 5) {
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                container.size();
                container.notifyAll();
            }
        }, "线程2").start();

        while (container.list.size() == 5){
            System.exit(0);
        }

    }


}

class Container {

    List<Integer> list = new CopyOnWriteArrayList<>();

    public synchronized void add(int i) {
        list.add(i);
    }

    public synchronized void size() {
        System.out.println("元素个数达到了: " + list.size());
    }

}