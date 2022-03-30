一、JUC

1. 书籍推荐 《java并发编程实战》
2. 进程与线程 ● 进程：进程是一个具有一定独立功能的程序关于某个数据集合的一次运行活动。它是操作系统动态执行的基本单元，在传统的操作系统中，进程既是基本的分配单元，也是基本的执行单元。 ●
   线程：通常在一个进程中可以包含若干个线程，当然一个进程中至少有一个线程，不然没有存在的意义。线程可以利用进程所拥有的资源，在引入线程的操作系统中，通常都是把进程作为分配资源的基本单位，而把线程作为独立运行和独立调度的基本单位，由于线程比进程更小，基本上不拥有系统资源，故对它的调度所付出的开销就会小得多，能更高效的提高系统多个程序间并发执行的程度。
3. 上下文切换

1. 上下文切换的概念
   即使是单核处理器也支持多线程执行代码，CPU通过给每个线程分配CPU时间片来实现这个机制。时间片是CPU分配给各个线程的时间，因为时间片非常短，所以CPU通过不停地切换线程执行，让我们感觉多个线程是同时执行的，时间片一般是几十毫秒（ms）。
   CPU通过时间片分配算法来循环执行任务，当前任务执行一个时间片后会切换到下一个任务。但是，在切换前会保存上一个任务的状态，以便下次切换回这个任务时，可以再加载这个任务的状态。所以任务从保存到再加载的过程就是一次上下文切换。这就像我们同时读两本书，当我们在读一本英文的技术书时，发现某个单词不认识，于是便打开中英文字典，但是在放下英文技术书之前，大脑必须先记住这本书读到了多少页的第多少行，等查完单词之后，能够继续读这本书。这样的切换是会影响读书效率的，同样上下文切换也会影响多线程的执行速度。

2. 如何减少上下文切换 减少上下文切换的方法有无锁并发编程、CAS算法、使用最少线程和使用协程。 无锁并发编程 ●
   多线程竞争锁时，会引起上下文切换，所以多线程处理数据时，可以用一些办法来避免使用锁，如将数据的ID按照Hash算法取模分段，不同的线程处理不同段的数据。 CAS算法（乐观锁） ●
   java的Atomic包使用CAS算法来更新数据，而不需要加锁。 使用最少线程 ● 避免创建不需要的线程，比如任务很少，但是创建了很多线程来处理，这 样会造成大量线程都处于等待状态。 协程 ●
   在单线程里实现多任务的调度，并在单线程里维持多个任务间的切换。

4. 线程的状态（重点）

a. 如何查看线程的状态 ● 1. 使用jps命令查看线程的线程号 ○ 再使用jstack查看线程某个时刻的运行情况（线程的快照） ● 2. jvisualvm-对线程进行dump查看线程的快照 b. 线程调用sleep()
● 线程进入TimeWaiting状态，不会释放锁 C. 线程调用wait()
● 线程进入Waiting状态，会释放锁 d. 当一个线程进入，另一个线程已经拿到锁的情况(synchronized)
● 另一个线程会进入blocked（阻塞）状态 e. 当一个线程进入，另一个线程已经拿到锁(Lock)
● 另一个线程进入Waiting状态 f. synchronized与Lock的区别 ● Lock锁更加的面向对象 ● 两个锁加锁后另一个线程进入的状态不同 ● synchronized进入blocked状态是被动地
还没有进入到同步代码块内 ● Lock锁进入Waiting主动的，已经进入到代码块里面(程序恢复执行之后 它会从刚刚暂停的地方恢复回来)
● synchronized 缺点 就算是去读也会进行加锁 ● lock 可以主动释放锁 能做synchronized做的所有事情，更加面向对象 ● jdk1.6以后对synchronized进行了优化 所以性能相差不大

5. synchronized(重点)
   ● 普通方法（对象方法）synchronized默认加锁对象是当前类的对象(this)
   ● 静态方法（类方法）synchronized默认加锁对象是当前类字节码对象(xxx.class)
   ● synchronized同步代码块加锁对象是指定的对象

6. synchronized的底层原理(重点)

● 利用反编译 javap -v xxx.class ● 它有一个monitorenter和monitorexit的过程 ● 如果一个线程拿到了monitor另外一个线程去拿就会失败，失败之后会进入到一个同步队列(阻塞)

7. 死锁 ● 双方各自持有对方需要的锁，不释放，双方都出于一种blocked状态

8. 守护线程 ● 如果被守护的线程终止，该守护线程也会立即终止

二、线程的通信（重点，会写代码）

1. 面试题 一共有3个线程，两个子线程先后循环2次，接着主线程循环3次，接着又回到两个子线程先后循环2次，再回到主线程又循环3次，如此循环5次。 public class Test01 {

   /*
   一共有3个线程，两个子线程先后循环2次，接着主线程循环3次， 接着又回到两个子线程先后循环2次，再回到主线程又循环3次，如此循环5次。
   */

   public static void main(String[] args) {

        Business business = new Business();

        // 整体循环5次
        for (int j = 0; j < 5; j++) {
            // 子线程1执行2次
            for (int i = 0; i < 2; i++) {
                business.child1();
            }
            // 子线程2执行2次
            for (int i = 0; i < 2; i++) {
                business.child2();
            }
            // 主线程1执行3次
            for (int i = 0; i < 3; i++) {
                business.main();
            }
        }

   }

}

/**

* 业务类
  */ class Business {

  Lock lock1 = new ReentrantLock(); Lock lock2 = new ReentrantLock(); Lock lock3 = new ReentrantLock();

  public void child1() { synchronized (lock1) { lock2.lock(); System.out.println("子线程1执行"); lock2.unlock(); } }

  public void child2() { synchronized (lock2) { lock3.lock(); System.out.println("子线程2执行"); lock3.unlock(); } }

  public void main() { synchronized (lock3) { lock1.lock(); System.out.println("主线程执行"); lock1.unlock(); } }

} ● 简化面试题 ○ 一个线程执行加法 0才能加 ○ 一个线程执行减法 1才能减 public class Communicate {

    /*
        线程通信
            题目： 一个线程进行加法 0才能加，另一个线程减法，1才能减
     */

    public static void main(String[] args) {
        Business business = new Business();

        new Thread(incr(business), "父线程1").start();
        new Thread(decr(business), "子线程1").start();
        // =======================================
        new Thread(incr(business), "父线程2").start();
        new Thread(decr(business), "子线程2").start();

    }

    /**
     * 加一的线程
     */
    private static Runnable decr(Business business) {
        return () -> {
            try {
                for (int i = 0; i < 6; i++) {
                    business.decr();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    /**
     * 减一的线程
     */
    private static Runnable incr(Business business) {
        return () -> {
            try {
                for (int i = 0; i < 6; i++) {
                    business.incr();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}

class Business {

    private int num = 0;

    /**
     * 加一的方法
     */
    public synchronized void incr() throws Exception {
        // 判断num当前值是否为0
        while (num != 0) {
            wait();
        }
        System.out.println(Thread.currentThread().getName() + "\t" + num);
        // 对num进行+1
        num++;
        // 唤醒另一个线程
        notifyAll();
    }

    /**
     * 减一的方法
     */
    public synchronized void decr() throws Exception {
        // 判断num当前值是否为1
        while (num != 1) {
            wait();
        }
        System.out.println(Thread.currentThread().getName() + "\t" + num);
        // 对num进行-1
        num--;
        // 唤醒另一个线程
        notifyAll();
    }

}

2. 扩展面试题 ○ 实现一个容器，提供两个方法，add，size 写两个线程，线程1添加10个元素到容器中， 线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束 public class Test02 {

   /*
   实现一个容器，提供两个方法，add，size 写两个线程，线程1添加10个元素到容器中， 线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
   */

   public static void main(String[] args) {

        Container container = new Container();

        new Thread(() -> {
            synchronized (container) {
                for (int i = 1; i <= 10; i++) {
                    if (container.list.size() == 5) {
                        try {
                            container.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("i = " + i);
                    container.add(i);
                    container.notify();
                }
            }
        }, "线程1").start();

        new Thread(() -> {
            synchronized (container) {
                while (container.list.size() != 5) {
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                container.size();
                container.notify();
            }
        }, "线程2").start();

   }

}

class Container {

    List<Integer> list = new CopyOnWriteArrayList<>();

    public synchronized void add(int i) {
        list.add(i);
    }

    public synchronized void size() {
        System.out.println("元素个数达到了: " + list.size());
    }

} ○ 线程1打印：ABCDE,线程2打印：12345。 两个线程交叉输出打印。 public class Test03 {

    /*
         线程1打印：ABCDE,线程2打印：12345。 两个线程交叉输出打印。
     */

    public static void main(String[] args) {

        char[] chars = new char[26];
        Object o = new Object();

        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ('A' + i);
        }

        new Thread(() -> {
            synchronized (o) {
                for (int i = 1; i <= chars.length; i++) {
                    System.out.println(i);
                    try {
                        o.notify();
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        }, "线程1").start();

        new Thread(() -> {
            synchronized (o) {
                for (char ch : chars) {
                    System.out.println(ch);
                    try {
                        o.notify();
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        }, "线程2").start();

    }

}

3. Wait与notify测试

public class Test08_WaitNotify {

    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) throws Exception {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();

        TimeUnit.SECONDS.sleep(1);

        Thread notifyThread = new Thread(new Notify(), "NotifyThread");
        notifyThread.start();
    }

    static class Wait implements Runnable {
        public void run() {
            synchronized (lock) {
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread() + "wait线程位置1");
                        // 释放锁
                        lock.wait();
                    } catch (InterruptedException e) {
                    }
                }
                // 条件满足时，完成工作
                System.out.println(Thread.currentThread() + "wait线程位置2 有可能最后执行");
            }
        }
    }

    static class Notify implements Runnable {
        public void run() {
            synchronized (lock) {
                System.out.println(Thread.currentThread() + "Notify线程位置1");
                // 不是马上释放该线程的执行权限 也不释放锁 通知wait的线程
                lock.notifyAll();
                flag = false;
                SleepUtils.second(5);
            }
            // 再次加锁
            synchronized (lock) {
                System.out.println(Thread.currentThread() + "Notify线程位置2再次加锁");
                SleepUtils.second(5);
            }
        }
    }

} 三、线程不安全（重重点）

1. 谈谈你对集合的理解

● 略

2. 说一下常见的异常有哪些 ● NullPointerException 空指针异常 ● ClassNotFoundException 指定类不存在 ● NumberFormatException 字符串转换为数字异常 ●
   IndexOutOfBoundsException 数组下标越界异常 ● ClassCastException 数据类型转换异常 ● FileNotFoundException 文件未找到异常 ●
   NoSuchMethodException 方法不存在异常 ● IOException IO 异常 ● SocketException Socket 异常
3. 说一下HashMap的底层原理 ●
   在HashMap采用HashCode将值散列到不同的位置，当然，采用hash算法可能会出现hash碰撞问题，在jdk1.7以前HashMap是采用数组+链表结构，这种结构会有一种链表的循环问题，当我们调用get方法的时候可能会出现死循环，这种不是太好，于是在jdk1.8版本HashMap采用的是数组+链表+红黑树的结构，这种结构可以解决死循环的问题。但是，在高并发情况下，还是会有死锁的问题，所以高并发情况下我们还是使用
   ConcurrentHashMap ，ConcurrentHashMap 采用的是细粒度锁，所以效率高。

● 1.7中采用数组+链表，1.8采用的是数组+链表/红黑树，即在1.8中链表长度超过一定长度后就改成红黑树存储。

1.7扩容时需要重新计算哈希值和索引位置，1.8并不重新计算哈希值，巧妙地采用和扩容后容量进行&操作来计算新的索引位置。

1.7是采用表头插入法插入链表，1.8采用的是尾部插入法。

在1.7中采用表头插入法，在扩容时会改变链表中元素原本的顺序，以至于在并发场景下导致链表成环的问题；在1.8中采用尾部插入法，在扩容时会保持链表元素原本的顺序，就不会出现链表成环的问题了。

四、八锁问题 public class Phone {

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

} public class Test01_Lock8 {

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

} 五、单例模式（手写）

1. 懒汉式 public class Test02_Singleton01 {

   private static Test02_Singleton01 instance = new Test02_Singleton01();

   private Test02_Singleton01() {

   }

   public static Test02_Singleton01 getInstance() { return instance; }

}

2. 饿汉式 public class Test02_Singleton02 {

   private static Test02_Singleton02 instance = null;

   private Test02_Singleton02() {

   }

   public synchronized static Test02_Singleton02 getInstance() { if (instance == null) { instance = new
   Test02_Singleton02(); } return instance; }

}

3. 双重检查 public class Test02_Singleton03 {

   /*
   为什么需要加上 volatile 关键字
    1. 为了解决JVM重排的问题 memory =allocate(); //1：分配对象的内存空间 ctorInstance(memory); //2：初始化对象 instanceA =memory;
       //3：设置instance指向刚分配的内存地址 ==========但是由于操作系统可以对指令进行重排序，所以上面的过程也可能会变成如下过程：========= memory =allocate();
       //1：分配对象的内存空间 instanceA =memory; //3：设置instance指向刚分配的内存地址 ctorInstance(memory); //2：初始化对象
       如果是这个流程，多线程环境下就可能将一个未初始化的对象引用暴露出来，从而导致不可预料的结果。 因此，为了防止这个过程的重排序，我们需要将变量设置为volatile类型的变量。
       */ private volatile static Test02_Singleton03 instance = null;

   private Test02_Singleton03() { }

   private static Test02_Singleton03 getInstance() { // 判断是否需要加锁，加锁消耗性能 if (instance == null) { synchronized (
   Test02_Singleton03.class) { // 判断是否需要创建对象 if (instance == null) { instance = new Test02_Singleton03(); } } } return
   instance; }

}

六、 Lock锁（会用）

1. 重入锁ReentrantLock ● 更加面向对象 ● 具备多路通知 ● 参考链接:
   ○  https://www.cnblogs.com/leesf456/p/5383609.html
   public class Test03_ReentrantLock {

   // 可重入锁 private Lock lock = new ReentrantLock(); // 通知队列 private Condition condition = lock.newCondition();

   public static void main(String[] args) { final Test03_ReentrantLock testLock = new Test03_ReentrantLock(); new
   Thread(() -> { try { testLock.method1(); } catch (Exception ignored) { } }, "AA").start(); SleepUtils.second(1); new
   Thread(testLock::method2, "BB").start(); }

   public void method1() throws Exception { lock.lock(); System.out.println("当前线程" + Thread.currentThread().getName()
   + "进入等待状态1"); SleepUtils.second(3); System.out.println("当前线程" + Thread.currentThread().getName() + "释放锁2");
   condition.await();// Object System.out.println("当前线程" + Thread.currentThread().getName() + "继续执行3"); lock.unlock(); }

   public void method2() { lock.lock(); System.out.println("当前线程" + Thread.currentThread().getName() + "进入等待状态4...");
   SleepUtils.second(3); System.out.println("当前线程" + Thread.currentThread().getName() + "发出唤醒5..."); condition.signal();
   lock.unlock(); }

} public class Test03_ReentrantLock2 {

    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();

    public static void main(String[] args) {

        final Test03_ReentrantLock2 umc = new Test03_ReentrantLock2();
        Thread t1 = new Thread(umc::m1, "t1");
        Thread t2 = new Thread(umc::m2, "t2");
        Thread t3 = new Thread(umc::m3, "t3");
        Thread t4 = new Thread(umc::m4, "t4");
        Thread t5 = new Thread(umc::m5, "t5");

        t1.start();    // c1  m1 await
        t2.start();    // c1  m2 await
        t3.start();    // c2  m3 await

        SleepUtils.second(2);

        t4.start();    // c1  singalAll

        SleepUtils.second(2);

        t5.start();    // c2 singal

    }

    public void m1() {

        try {
            lock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "进入方法m1等待..");
            c1.await();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "方法m1继续..");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void m2() {
        try {
            lock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "进入方法m2等待..");
            c1.await();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "方法m2继续..");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void m3() {
        try {
            lock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "进入方法m3等待..");
            c2.await();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "方法m3继续..");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void m4() {
        try {
            lock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "唤醒..");
            c1.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void m5() {
        try {
            lock.lock();
            System.out.println("当前线程：" + Thread.currentThread().getName() + "唤醒..");
            c2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}

2. 读写锁ReentrantReadWriteLock ● 读读共享，读写互斥，写写互斥 ● 读写锁一种特殊的锁，适用于高并发情况下， 读多写少的情况 ● 参考链接：
   ○  https://www.cnblogs.com/leesf456/p/5419132.html
   ○ https://blog.csdn.net/jiankunking/article/details/83954263
   public class Test04_ReadWriteLock {

   /*
   读写锁 读读共享，读写互斥，写写互斥 读写锁是一种特殊的锁，适用于高并发情况下，读多写少的情况
   */

   private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(); private ReentrantReadWriteLock.ReadLock
   readLock = rwLock.readLock(); private ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

   public static void main(String[] args) {

        final Test04_ReadWriteLock rwl = new Test04_ReadWriteLock();

        // 读线程
        Thread t1 = new Thread(rwl::read, "t1");
        Thread t2 = new Thread(rwl::read, "t2");

        // 写线程
        Thread t3 = new Thread(rwl::write, "t3");
        Thread t4 = new Thread(rwl::write, "t4");

// t1.start();//R // t2.start();//R

        t1.start(); // R
        t3.start(); // W

// t3.start();// W // t4.start();// W }

    public void read() {
        readLock.lock();
        String threadName = Thread.currentThread().getName();
        System.out.println("当前线程:" + threadName + "读进入...");
        SleepUtils.second(3);
        System.out.println("当前线程:" + threadName + "读退出...");
        readLock.unlock();
    }

    public void write() {
        writeLock.lock();
        String name = Thread.currentThread().getName();
        System.out.println("当前线程:" + name + "写进入...");
        SleepUtils.second(3);
        System.out.println("当前线程:" + name + "写退出...");
        writeLock.unlock();
    }

}

3. 编写一个缓存系统 public class Test05_MyCache {

   // 读写锁 private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(); // 缓存 private Map<String, Object> cache
   = new HashMap<>();

   public static void main(String[] args) {

        Test05_MyCache myCache = new Test05_MyCache();
        Object data1 = myCache.getData("lucky845");
        Object data2 = myCache.getData("lucky845");
        System.out.println("data1 = " + data1);
        System.out.println("data2 = " + data2);

   }

   public Object getData(String key) {

        Object value = null;

        try {
            // 读锁
            rwLock.readLock().lock();
            value = cache.get(key);
            rwLock.readLock().unlock();

            // 判断是否需要加锁
            if (value == null) {
                // 写锁
                rwLock.writeLock().lock();
                // 是否需要查询数据库
                if (value == null) {
                    // 写操作
                    value = "you are so intelligent";
                    cache.put(key, value);
                }
                rwLock.writeLock().unlock();
            }

            rwLock.readLock().lock();
        } finally {
            rwLock.readLock().unlock();
        }
        return value;
   }

}

4. volatile关键字(重点)
   a. 防止指令重排 ● https://www.cnblogs.com/bbgs-xc/p/12731769.html
   ● 在指令的地方插入内存屏障，防止内存屏障前后指令进行重排优化 ● 参考双重检查使用的volatile关键字 b. 可见性 ● https://www.cnblogs.com/jpfss/p/9956172.html
   ● 当一个线程修改了一个共享变量的值之后，其他线程可以立马感知到该值改变之后的内容 public class Test06_Volatile {

   /*
   volatile关键字 当一个线程修改了一个共享变量的值之后，其他线程 可以立马感知到该值改变之后的内容
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

   public Integer getA() { return a; }

   public void setA(Integer a) { this.a = a; }

}

七、java中的阻塞队列（理解概念）

1. 概念 ● 队列为空，取数据操作就会阻塞 ● 队列为满，放数据操作就会阻塞
2. 常见阻塞队列 ● ArrayBlockingQueue ○ 数组实现的有界阻塞，指定数组的长度 ● LinkedBlockingQueue ○ 一个由链表结构组成的有界阻塞队列，Integer.MAX_VALUE ●
   SynchronousQueue ○ 一个不存储元素的阻塞队列 ○ 只是起到一个传递数据的作用，必须取和存数据的线程同时存在 ，才能往里面放数据 public class Test07_BlockingQueue {

   /*
   阻塞队列
    1. ArrayBlockingQueue： 数组实现的有界阻塞，指定数组的长度
    2. LinkedBlockingQueue： 一个由链表结构组成的有界阻塞队列，最大长度为 Integer.MAX_VALUE
    3. SynchronousQueue： 一个不存储元素的阻塞队列，只是起到一个传递数据的作用， 必须取和存数据的线程同时存在才能往里面放数据

       阻塞队列的原理(重点)
       借助重入锁(乐观锁)+线程通信 ArrayBlockingQueue的put和take使用的是同一把锁 put和take不能同时操作 LinkedBlockingQueue的put和take使用的是不同的锁
       put和take能同时操作
       */

   public static void main(String[] args) throws InterruptedException {

        BlockingQueue blockingQueue = new ArrayBlockingQueue(3);

        // 使用add()与remove()

// System.out.println(blockingQueue.add("a")); // System.out.println(blockingQueue.add("b")); // System.out.println(
blockingQueue.add("c")); // 如果队列已满，抛出异常 // System.out.println(blockingQueue.add("d")); // System.out.println(
blockingQueue.remove()); // System.out.println(blockingQueue.remove()); // System.out.println(blockingQueue.remove());
// 如果队列为空，抛出异常 // System.out.println(blockingQueue.remove());

        // 使用offer()和poll()

// System.out.println(blockingQueue.offer("a")); // System.out.println(blockingQueue.offer("b")); // System.out.println(
blockingQueue.offer("c")); // 如果队列已满，返回false // System.out.println(blockingQueue.offer("d")); // System.out.println(
blockingQueue.remove()); // System.out.println(blockingQueue.remove()); // System.out.println(blockingQueue.remove());
// 如果队列为空，返回null // System.out.println(blockingQueue.remove());

        // 使用put()和take()

// blockingQueue.put("a"); // blockingQueue.put("b"); // blockingQueue.put("c"); // 如果队列已满，阻塞队列 // blockingQueue.put("
d"); // System.out.println(blockingQueue.take()); // System.out.println(blockingQueue.take()); // System.out.println(
blockingQueue.take()); // 如果队列为空，阻塞队列 // System.out.println(blockingQueue.take());

        // 使用offer()和poll()并设置超时时间
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        //如果队列已满 返回false
        System.out.println(blockingQueue.offer("d", 3, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        //如果队列已空 返回null
        System.out.println(blockingQueue.poll(3, TimeUnit.SECONDS));

    }

}

3. 常见方法 ● add/remove ○ 如果队列满了 会抛出异常 ○ 如果队列空了 会抛出异常 ● offer/poll ○ 如果队列满了 返回false ○ 如果队列空了 返回一个null ● put/take ○ 如果队列满了
   阻塞 阻塞队列 ○ 如果队列空了 阻塞 阻塞队列
4. 阻塞队列的原理(重点)
   ● 借助重入锁(乐观锁)+线程通信 ● ArrayBlockingQueue的put和take使用的是同一把锁 put和take不能同时操作 ● LinkedBlockingQueue的put和take使用的是不同的锁
   put和take能同时操作

八、CAS(重点)
1.synchronized性能问题 ● 没有得到锁资源的线程进入BLOCKED状态 ● 争夺到锁资源后恢复为RUNNABLE状态 ● 在这个过程中涉及到用户模式和内核模式的转换,消耗性能 2.CAS的原理 ●
CAS机制中使用了3个操作数:内存地址V，旧的预期值A，要修改的新值B ● 当我们要去更新一个值的时候 会把旧的预期值拿到同内存地址的值进行比较 ● 如果相同才进行置换 ● unsafe为我们提供了基于硬件级别的原子操作 3.CAS的缺点
● a. CPU开销较大 ○ for(;;)和while(true)的区别? ■ for(;;)编译后指令更少 效率更高
■ https://blog.csdn.net/qq_36381855/article/details/79934629
● b. 不能保证代码块的原子性 ● c. ABA问题 4.ABA问题 a.概念 ● 一个变量从A->B->A的过程 b.实际生活场景 ● 在一台机器上取钱 100-50 机器卡住(100-50)待执行 ● 又去其他机器上取钱
100-50=50成功 ● 前女友给他转50 50+50=100 ● 最开始的哪个机器复活了 ABA出现 c.解决方案 ● 加一个版本号 5.CAS与synchronized的比较 ● 在并发度不是太高的情况下
CAS效率synchronized高 ● 而在并发度特别高 synchronized效率更高 ● https://blog.csdn.net/lsx2017/article/details/105375425

九、 线程池（重点）

1. 概念 ● 对比数据库连接池、redis连接池，先准备好一些资源，有任务来了直接处理，处理完任务之后不释放资源，资源可以复用
2. 功能 ● 线程池的主要工作是控制运行的线程数量，处理过程中将任务放入队列，然后在线程创建后启动这些任务，如果线程数量超过了最大数量，超出数量的线程排队等待，等其他线程执行完毕，再从队列中取出任务来执行。 ● 主要特点为：
   线程复用、控制最大并发数、管理线程。 ○ 1. 降低资源消耗。通过重复利用已创建的线程降低创建线程和销毁线程造成的消耗。 ○ 2. 提高相应速度。当任务到达时，任务可以不需要等待线程创建就能立即执行。 ○ 3.
   提高线程的可管理性。线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一分配，调优和监控。
3. 创建线程池

● a. Executors.newFixedThreadPool(int)
○ 创建一个固定数量的线程池 适用于执行长期任务 ● b. Executors.newSingleThreadExecutor()
○ 创建一个单线程的线程池 任何时候都只有一个线程在运行 就保证了执行线程的顺序性 ● c. Executors.newCachedThreadPool()
○ 创建一个可扩展的线程池 适用于短时异步任务 public class Test01_ThreadPool {

    public static void main(String[] args) {

        // 1. 创建一个固定数量的线程池，适用于执行长期任务
        ExecutorService threadPool1 = Executors.newFixedThreadPool(3);
        // 2. 创建一个单个线程的线程池，任何时候都只有一个线程在执行，能够保证任务具备一定的顺序
        ExecutorService threadPool2 = Executors.newSingleThreadExecutor();
        // 3. 创建一个可扩展的线程池，适合于短时异步任务
        ExecutorService threadPool3 = Executors.newCachedThreadPool();
        // 4. 创建一个可以定时操作的线程池，定时任务
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);

        for (int i = 1; i <= 100; i++) {
            int a = i;
            threadPool.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "银行柜台为第" + a + "位用户服务");
            });
        }
        threadPool.shutdown();

    }

}

4. 实际使用的线程池

public class Test02_ThreadPool {

    public static void main(String[] args) {

        /**
         * @param corePoolSize 核心线程数
         * @param maximumPoolSize 最大线程数
         * @param keepAliveTime 存活时间
         * @param unit 时间单位
         * @param workQueue 阻塞队列
         * @param handler 拒绝策略
         */
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,
                5,
                3,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        /**
         * 1. 办理业务的人<=5,开启两个线程
         * 2. 办理业务的人<=8,开启<=5个线程
         * 3. 办理业务的人>8,产生拒绝策略
         *      默认的拒绝策略是： AbortPolicy() 直接抛出异常
         *                      DiscardPolicy() 丢弃新任务
         *                      DiscardOldestPolicy() 丢弃老任务，抛弃在队列中等待时间较长的老任务，适用于时效性较高的场景
         *                      CallerRunsPolicy()   任务来自于哪里，就交给哪里，能够最大限度的处理业务
         */
        for (int i = 1; i <= 10; i++) {
            int a = i;
            threadPool.submit(() -> {
                SleepUtils.second(2);
                System.out.println(Thread.currentThread().getName() + "银行柜台为第" + a + "位用户服务");
            });
        }
        threadPool.shutdown();

    }

}

5. ThreadPoolExecutor线程池的参数 ● corePoolSize ○ 线程池中的常驻核心线程数 ● maximumPoolSize ○ 线程池中能够容纳同时执行的最大线程数,此值必须大于等于1 ●
   keepAliveTime ○ 空闲线程的存活时间,当前池中线程数量超过corePoolSize时,当空闲时间达到keepAliveTime时,空闲线程会被销毁直到只剩下corePoolSize个线程为止 ● unit ○
   keepAliveTime的单位 ● workQueue ○ 任务队列,被提交但尚未被执行的任务 ● threadFactory ○ 表示生成线程池中工作线程的线程工厂,用于创建线程,一般默认的即可 ● handler ○
   拒绝策略,表示当队列满了,并且工作线程大于等于线程池的最大线程数(maximumPoolSize)时如何拒绝请求执行的runnable的策略

十、 线程池的原理（重点）

1. 线程池执行过程

1. 在创建了线程池后,线程池中的线程数为零。
2. 当调用execute()方法添加一个请求任务时,线程池会做出如下判断： a. 如果正在运行的线程数量小于corePoolSize,那么马上创建线程运行这个任务； b.
   如果正在运行的线程数量大于或等于corePoolSize,那么将这个任务放入队列； c. 如果这个时候队列满了且正在运行的线程数量还小于maximumPoolSize,那么还是要创建非核心线程立刻运行这个任务； d.
   如果队列满了且正在运行的线程数量大于或等于maximumPoolSize,那么线程池会启动饱和拒绝策略来执行。
3. 当一个线程完成任务时,它会从队列中取下一个任务来执行。
4. 当一个线程无事可做超过一定的时间(keepAliveTime)时,线程会判断： 如果当前运行线程数大于corePoolSize,那么这个线程就被停掉。 所以线程池所有任务完成后,它最终会收缩到corePoolSize大小。
2. 线程池的状态

1. RUNNING： 能接受新提交的任务,并且也能够处理阻塞队列中的任务
2. SHUTDOWN：不再接受新提交的任务,但是可以处理存量任务(即阻塞队列中的任务)
3. STOP： 不再接受新提交的任务,也不处理量任务
4. TIDYING：所有任务都已终止
5. TERMINATED： 默认是什么也不做的,只是作为一个标识
3. 线程池的拒绝策略 等待队列已经排满了,再也塞不下新任务了,同时线程池中的max线程也达到了,无法继续为新任务服务。这个是时候我们就需要拒绝策略机制合理的处理这个问题。
1. AbortPolicy(默认)：直接抛出RejectedExecutionException异常阻止系统正常运行
2. CallerRunsPolicy："调用者运行"一种调节机制，该策略既不会抛弃任务，也不 会抛出异常，而是将某些任务回退到调用者，从而降低新任务的流量。
3. DiscardOldestPolicy：抛弃队列中等待最久的任务，然后把当前任务加人队列中尝 试再次提交当前任务。
4. DiscardPolicy：该策略默默地丢弃无法处理的任务，不予任何处理也不抛出异常。 如果允许任务丢失，这是最好的一种策略。

十一、Callable和Runnable（重点）

1. 面试题：创建线程的方法有？
1. 继承 Thread类
2. 实现Runnable接口
3. JDK1.5后实现Callable接口
4. 使用java的线程池
2. 面试题：Callable接口与Rannable接口有什么区别？
1. Callable接口的call()方法有返回值，而Runnable接口的run()方法没有返回值
2. Callable接口会抛异常，Runnable接口不会抛异常
3. 实现的方法不同，Callable是call()方法，Runnable是run()方法
3. FutureTask 在主线程中需要执行比较耗时的操作时，但又不想阻塞主线程时，可以把这些作业交给Future对象在后台完成，当主线程将来需要时，就可以通过Future对象获得后台作业的计算结果或者执行状态。
   一般FutureTask多用于耗时的计算，主线程可以在完成自己的任务后，再去获取结果。仅在计算完成时才能检索结果；如果计算尚未完成，则阻塞 get
   方法。一旦计算完成，就不能再重新开始或取消计算。get方法而获取结果只有在计算完成时获取，否则会一直阻塞直到任务转入完成状态，然后会返回结果或者抛出异常。 特点:只计算一次，get方法放到最后 public class
   Test03_Callable {

   public static void main(String[] args) throws Exception {

        new Thread(new MyRunnable(), "Runnable").start();

        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());
        new Thread(futureTask, "Callable").start();
        System.out.println(futureTask.get());
        // 阻塞方法，结果可以复用
        System.out.println(futureTask.get());

        FutureTask<String> task = new FutureTask<>(new MyRunnable(), "lucky845");
        new Thread(task, "Runnable in FutureTask").start();
        System.out.println(task.get());

   }

}

class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("MyRunnable is Running");
    }

}

class MyCallable implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("MyCallable is Running");
        return "lucky845";
    }

}

十二、JUC的工具类（会用）

1. CyclicBarrier ● CountDownLatch主要有两个方法，当一个或多个线程调用await方法时，这些线程会阻塞。其它线程调用countDown方法会将计数器减1(调用countDown方法的线程不会阻塞)
   ，当计数器的值变为0时，因await方法阻塞的线程会被唤醒，继续执行。 ● 所有资源到位之后一起去做某件事情，资源之间要相互等待 public class Test04_CyclicBarrier {

   public static void main(String[] args) {

        /*
            CyclicBarrier： 所有资源到位后一起去做某件事，资源之间需要互相等待
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println("召唤神龙");
        });

        for (int i = 1; i <= 7; i++) {
            SleepUtils.second(2);
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "号龙珠收集完成");
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }

   }

}

2. CountDownLatch ● CyclicBarrier的字面意思是可循环(Cyclic)使用的屏障(Barrier)。它要做的事情是，让一组线程到达一个屏障(也可以叫同步点)
   时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续干活。线程进入屏障通过CyclicBarrier的await()方法。 ● 一个程序需要等待程序做完某些事情之后才能做某件事情 public class
   Test05_CountDownLatch {

   public static void main(String[] args) throws Exception {

        /*
            CountDownLatch: 一个程序需要等待程序做完某些事情之后才能做某件事情
         */

        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            SleepUtils.second(2);
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "号同学离开教室");
                // 计数器减一
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }
        // 等待计数器清零
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "班长离开教师");

   }

}

3. Semaphore ● 在信号量上我们定义两种操作： acquire（获取）
   当一个线程调用acquire操作时，它要么通过成功获取信号量（信号量减1），要么一直等下去，直到有线程释放信号量，或超时。release（释放）实际上会将信号量的值加1，然后唤醒等待的线程。 ●
   信号量主要用于两个目的，一个是用于多个共享资源的互斥使用，另一个用于并发线程数的控制。 ● 资源有限 大家要抢夺资源 只用一会儿 public class Test06_Semaphore {

   public static void main(String[] args) {

        /*
            Semaphore: 信号量
                资源有限，大家要抢夺资源，只用一会
         */

        Semaphore semaphore = new Semaphore(3);

        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {

                try {
                    // 获取资源
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "号车进入停车位");
                    SleepUtils.second(2);
                    System.out.println(Thread.currentThread().getName() + "号车离开停车位");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 释放资源
                    semaphore.release();
                }

            }, String.valueOf(i)).start();
        }

   }

}

十三、fork/join框架 Fork/Join框架是Java7提供了的一个用于并行执行任务的框架， 是一个把大任务分割成若干个小任务，最终汇总每个小任务结果后得到大任务结果的框架。
我们再通过Fork和Join这两个单词来理解下Fork/Join框架，Fork就是把一个大任务切分为若干子任务并行的执行，Join就是合并这些子任务的执行结果，最后得到这个大任务的结果。比如计算1+2+。。＋10000，可以分割成10个子任务，每个子任务分别对1000个数进行求和，最终汇总这10个子任务的结果。Fork/Join的运行流程图如下：

Java提供了ForkJoinPool来支持将一个任务拆分成多个“小任务”并行计算，再把多个“小任务”的结果合成总的计算结果，RecursiveTask代表有返回值的任务 public class Test07_ForkJoin {

    /*
        代码需求： 将1+2+3+4+。。。+ n 的和
            使用fork/join框架将大任务拆分为小任务
     */

    public static void main(String[] args) throws Exception {

        MyTask myTask = new MyTask(1, 10000);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(myTask);
        System.out.println(forkJoinTask.get());

    }

}

class MyTask extends RecursiveTask<Integer> {

    /**
     * 开始的数
     */
    private final int begin;

    /**
     * 最后一个数
     */
    private final int end;

    /**
     * 返回的结果
     */
    private int result;

    public MyTask(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    /**
     * 这是一个递归方法！
     */
    @Override
    protected Integer compute() {

        /*
         * 递归的出口阈值
         */
        int THRESHOLD = 10;

        if ((end - begin) < THRESHOLD) {
            for (int i = begin; i <= end; i++) {
                result += i;
            }
        } else {
            int mid = (begin + end) >> 1;
            /*
             * 拆分的前半部分 1-5000
             */
            MyTask task1 = new MyTask(begin, mid);
            /*
             * 拆分的后半部分 5001-10000
             */
            MyTask task2 = new MyTask(mid + 1, end);
            /*
                异步执行任务
             */
            task1.fork();
            task2.fork();
            result = task1.join() + task2.join();
        }
        return result;
    }

}

十四、面试题
https://www.cnblogs.com/crazymakercircle/p/14655412.html
