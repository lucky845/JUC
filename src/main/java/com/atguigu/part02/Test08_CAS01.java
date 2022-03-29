package com.atguigu.part02;

import com.atguigu.util.SleepUtils;

/**
 * @author lucky845
 * @date 2022年03月29日 17:02
 */
public class Test08_CAS01 {

    private static int count = 0;

    public static void main(String[] args) {

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < 100; j++) {
                    // 不符合原子性，有加一和赋值两个步骤
                    count++;
                }
            }).start();
        }

        SleepUtils.second(2);

        System.out.println("count = " + count);
    }

}
