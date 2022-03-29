package com.atguigu.part02;

import com.atguigu.part02.pojo.Phone;
import com.atguigu.util.SleepUtils;

/**
 * @author lucky845
 * @date 2022年03月29日 15:53
 */
public class Test01_Lock8 {

    /*
        8锁问题
            1.标准访问，先打印短信还是邮件
                时间片不确定，先后顺序不确定
            2.停4秒在短信方法内，先打印短信还是邮件
                短信先打印，因为sleep()不会释放锁资源
            3.新增普通的hello方法，是先打短信还是hello
                hello，因为hello()没有锁，不受锁的控制
            4.现在有两部手机，先打印短信还是邮件
                邮件先打印，因为两个锁互不影响，但是短信方法会sleep4秒
            5.两个静态同步方法，1部手机，先打印短信还是邮件
                短信先打印，因为这时的锁是当前类的字节码对象(.class)，所以是用的同一个锁
            6.两个静态同步方法，2部手机，先打印短信还是邮件
                短信先打印，因为这时的锁是当前类的字节码对象(.class)，所以是用的同一个锁
            7.1个静态同步方法,1个普通同步方法，1部手机，先打印短信还是邮件
                邮件先打印，因为两个线程的锁不一样，一个是字节码对象，一个是类对象
            8.1个静态同步方法,1个普通同步方法，2部手机，先打印短信还是邮件
                邮件先打印，因为两个线程的锁不一样，一个是字节码对象，一个是类对象
     */
    public static void main(String[] args) {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();
        new Thread(() -> {
            phone1.sendMsg();
        }).start();
        SleepUtils.second(1);
        new Thread(() -> {
            phone2.hello();
        }).start();
    }

}
