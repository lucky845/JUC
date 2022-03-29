package com.atguigu.part02;

/**
 * 双重检查
 *
 * @author lucky845
 * @date 2022年03月29日 16:09
 */
public class Test02_Singleton03 {

    /*
        为什么需要加上 volatile 关键字
            1. 为了解决JVM重排的问题
                memory =allocate();    //1：分配对象的内存空间
                ctorInstance(memory);  //2：初始化对象
                instanceA =memory;     //3：设置instance指向刚分配的内存地址
                ==========但是由于操作系统可以对指令进行重排序，所以上面的过程也可能会变成如下过程：=========
                memory =allocate();    //1：分配对象的内存空间
                instanceA =memory;     //3：设置instance指向刚分配的内存地址
                ctorInstance(memory);  //2：初始化对象
        如果是这个流程，多线程环境下就可能将一个未初始化的对象引用暴露出来，从而导致不可预料的结果。
        因此，为了防止这个过程的重排序，我们需要将变量设置为volatile类型的变量。
     */
    private volatile static Test02_Singleton03 instance = null;

    private Test02_Singleton03() {
    }

    private static Test02_Singleton03 getInstance() {
        // 判断是否需要加锁，加锁消耗性能
        if (instance == null) {
            synchronized (Test02_Singleton03.class) {
                // 判断是否需要创建对象
                if (instance == null) {
                    instance = new Test02_Singleton03();
                }
            }
        }
        return instance;
    }

}
