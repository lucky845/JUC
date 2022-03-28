package com.atguigu.part01.interview;

/**
 * @author lucky845
 * @date 2022年03月28日 20:36
 */
public class Test01 {

    /*
        一共有3个线程，两个子线程先后循环2次，接着主线程循环3次，
        接着又回到两个子线程先后循环2次，再回到主线程又循环3次，如此循环5次。
     */

    public static void main(String[] args) {
        Business business = new Business();
        // 整体循环5次
        for (int j = 0; j < 5; j++) {
            // 子线程1执行2次
            new Thread(business::child1).start();
            // 子线程2执行2次
            new Thread(business::child2).start();
            // 主线程1执行3次
            new Thread(business::main).start();
        }
    }

}

/**
 * 业务类
 */
class Business {

    // 标记执行者
    private int flag = 1;

    public synchronized void child1() {
        for (int i = 0; i < 2; i++) {
            while (flag != 1) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("子线程1执行");
        }
        flag = 2;
    }

    public synchronized void child2() {
        for (int i = 0; i < 2; i++) {
            while (flag != 2) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("子线程2执行");
        }
        flag = 3;

    }

    public synchronized void main() {
        for (int i = 0; i < 3; i++) {
            while (flag != 3) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("主线程执行");
        }
        flag = 1;
    }

}
