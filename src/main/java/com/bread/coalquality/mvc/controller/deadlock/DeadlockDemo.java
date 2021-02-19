package com.bread.coalquality.mvc.controller.deadlock;

/**
 * @Description: 死锁demo
 * <p> 概念
 * 死锁是指两个或多个以上的进程在执行过程中，因争夺资源而造成一种互相等待的现象，若无外力干涉那他们都将无法推进下去。
 * 如果资源充足，进程的资源请求都能够得到满足，死锁出现的可能性就很低，否则就会因争夺有限的资源而陷入死锁。
 * </>
 * <p> 产生的原因
 * 系统资源不足
 * 进程运行推进的顺序不对
 * 资源分配不当
 * </>
 * <p> 如何解决
 * 1> 当我们出现死锁的时候，首先需要使用jps命令查看运行的程序 ->jps -l
 * 2> 在使用jstack查看堆栈信息 jstack  17808   # 后面参数是 jps输出的该类的pid
 * 3> 查看后会告诉我们
 * Java stack information for the threads listed above:
 *   ===================================================
 *  "t2":
 *  at com.bread.coalquality.mvc.controller.deadlock.DeadlockDemo.run(DeadlockDemo.java:55)
 *  - waiting to lock <0x000000076c4990d8> (a java.lang.String)
 *  - locked <0x000000076c499110> (a java.lang.String)
 *  at java.lang.Thread.run(Thread.java:748)
 *  "t1":
 *  at com.bread.coalquality.mvc.controller.deadlock.DeadlockDemo.run(DeadlockDemo.java:55)
 *  - waiting to lock <0x000000076c499110> (a java.lang.String)
 *  - locked <0x000000076c4990d8> (a java.lang.String)
 *  at java.lang.Thread.run(Thread.java:748)
 *
 *  Found 1 deadlock.
 *
 *  55 行的代码  线程T2和T1 分别要获取对方的锁，最后一行提示我们有一处死锁
 * </>
 * @Author: haoxd
 * @Version: 1.0
 */
public class DeadlockDemo implements Runnable {


    private Object lockA = new Object();
    private Object lockB = new Object();


    public DeadlockDemo(Object lockA, Object lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {


        synchronized (lockA) {

            System.out.println(Thread.currentThread().getName() + "\t 自己持有" + lockA + "\t 尝试获取：" + lockB);

            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + "\t 自己持有" + lockB + "\t 尝试获取：" + lockA);
            }

        }


    }

    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";

        new Thread(new DeadlockDemo(lockA, lockB), "t1").start();

        new Thread(new DeadlockDemo(lockB, lockA), "t2").start();
    }
}
