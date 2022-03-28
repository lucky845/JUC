package com.atguigu.part01;

import com.atguigu.util.SleepUtils;

/**
 * @author lucky845
 * @date 2022年03月28日 18:51
 */
public class Test06_Daemon {

    /*
        守护线程
            如果被守护的线程终止 该守护线程也会终止
     */

    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner());
        // 设置该线程为守护线程
        thread.setDaemon(true);
        thread.start();
        SleepUtils.second(2);
        System.out.println("main线程结束...");
    }

    static class DaemonRunner implements Runnable {

        @Override
        public void run() {
            System.out.println("守护线程开始睡觉...");
            try {
                SleepUtils.second(5);
            } finally {
                System.out.println("守护线程最终执行...");
            }
        }
    }

}
