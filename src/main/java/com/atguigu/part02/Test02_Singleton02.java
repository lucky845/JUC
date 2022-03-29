package com.atguigu.part02;

/**
 * 懒汉式
 *
 * @author lucky845
 * @date 2022年03月29日 16:07
 */
public class Test02_Singleton02 {

    private static Test02_Singleton02 instance = null;

    private Test02_Singleton02() {

    }

    public synchronized static Test02_Singleton02 getInstance() {
        if (instance == null) {
            instance = new Test02_Singleton02();
        }
        return instance;
    }

}
