package com.atguigu.part02;

/**
 * 饿汉式： 线程安全且简单的模式
 *
 * @author lucky845
 * @date 2022年03月29日 16:04
 */
public class Test02_Singleton01 {

    private static Test02_Singleton01 instance = new Test02_Singleton01();

    private Test02_Singleton01() {

    }

    public static Test02_Singleton01 getInstance() {
        return instance;
    }

}
