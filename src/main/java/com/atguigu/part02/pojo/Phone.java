package com.atguigu.part02.pojo;

import com.atguigu.util.SleepUtils;

/**
 * @author lucky845
 * @date 2022年03月29日 15:53
 */
public class Phone {

    public static synchronized void sendMsg() {
        SleepUtils.second(4);
        System.out.println("发送短信1");
    }

    public static synchronized void hello() {
        System.out.println("hello");
    }

    public synchronized void sendEmail() {
        System.out.println("发送邮箱2");
    }

}
