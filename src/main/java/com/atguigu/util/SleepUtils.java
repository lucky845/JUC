package com.atguigu.util;

import java.util.concurrent.TimeUnit;

/**
 * @author lucky845
 * @date 2022年03月28日 9:50
 */
public class SleepUtils {

    public static final void second(long second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
