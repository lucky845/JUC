package com.atguigu.part02;

import com.atguigu.util.SleepUtils;

/**
 * @author lucky845
 * @date 2022年03月29日 16:41
 */
public class Test06_Volatile {

    /*
        volatile关键字
            当一个线程修改了一个共享变量的值之后，其他线程
			可以立马感知到该值改变之后的内容
     */

    private volatile Integer a = 0;

    public static void main(String[] args) {

        Test06_Volatile test06 = new Test06_Volatile();

        new Thread(() -> {
            test06.setA(3);
        }, "AA").start();

        SleepUtils.second(1);

        new Thread(() -> {
            System.out.println(test06.getA());
        }, "BB").start();

    }

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

}
