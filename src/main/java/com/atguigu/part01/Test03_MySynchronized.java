package com.atguigu.part01;

/**
 * @author lucky845
 * @date 2022年03月28日 17:56
 */
public class Test03_MySynchronized {

    /*
        synchronized 底层原理
            1. 使用 javap -v xxx.class 指令反编译查看字节码文件
            2. 它有一个 monitorenter 和 monitorexit 的过程
            3. 如果一个线程拿到了 monitor 另外一个线程去拿就会失败，失败之后会进入到一个同步队列(阻塞)
     */

    public static void main(String[] args) {
        // 对 Test03_MySynchronized Class 对象进行加锁
        synchronized (Test03_MySynchronized.class){
            System.out.println("lucky845");
        }
        // 静态同步方法，对 synchronized class 对象进行加锁
        m();
    }

    public static synchronized void m(){
        System.out.println("online");
    }

}
