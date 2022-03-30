package com.atguigu.part03;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author lucky845
 * @date 2022年03月30日 18:19
 */
public class Test07_ForkJoin {

    /*
        代码需求： 将1+2+3+4+。。。+ n 的和
            使用fork/join框架将大任务拆分为小任务
     */

    public static void main(String[] args) throws Exception {

        MyTask myTask = new MyTask(1, 10000);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(myTask);
        System.out.println(forkJoinTask.get());

    }

}

class MyTask extends RecursiveTask<Integer> {

    /**
     * 开始的数
     */
    private final int begin;

    /**
     * 最后一个数
     */
    private final int end;

    /**
     * 返回的结果
     */
    private int result;

    public MyTask(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    /**
     * 这是一个递归方法！
     */
    @Override
    protected Integer compute() {

        /*
         * 递归的出口阈值
         */
        int THRESHOLD = 10;

        if ((end - begin) < THRESHOLD) {
            for (int i = begin; i <= end; i++) {
                result += i;
            }
        } else {
            int mid = (begin + end) >> 1;
            /*
             * 拆分的前半部分 1-5000
             */
            MyTask task1 = new MyTask(begin, mid);
            /*
             * 拆分的后半部分 5001-10000
             */
            MyTask task2 = new MyTask(mid + 1, end);
            /*
                异步执行任务
             */
            task1.fork();
            task2.fork();
            result = task1.join() + task2.join();
        }
        return result;
    }
}