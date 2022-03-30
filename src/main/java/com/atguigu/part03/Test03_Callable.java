package com.atguigu.part03;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author lucky845
 * @date 2022年03月30日 16:51
 */
public class Test03_Callable {

    public static void main(String[] args) throws Exception {

        new Thread(new MyRunnable(), "Runnable").start();

        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());
        new Thread(futureTask, "Callable").start();
        System.out.println(futureTask.get());
        // 阻塞方法，结果可以复用
        System.out.println(futureTask.get());

        FutureTask<String> task = new FutureTask<>(new MyRunnable(), "lucky845");
        new Thread(task, "Runnable in FutureTask").start();
        System.out.println(task.get());

    }

}

class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("MyRunnable is Running");
    }
}

class MyCallable implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("MyCallable is Running");
        return "lucky845";
    }
}
