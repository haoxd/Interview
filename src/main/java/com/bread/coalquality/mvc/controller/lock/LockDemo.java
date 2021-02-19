package com.bread.coalquality.mvc.controller.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

/**
 * @Description://默認非公平鎖 俩个构造方法
 * <p>
 * <p>
 * 1 公平锁：多个线程按照申请锁的顺序来获取锁
 * <p>
 * 2 非公平锁：多个线程不是按照申请锁的顺序，在高并发的情况下，有可能造成优先级反转和饥饿的现象，有点在于高并发环境下
 * 吞吐量比公平锁大s
 * synchronized (也是一种非公平锁)
 * <p>
 * 3 可重入锁（也叫递归锁）： 同一个线程外层函数获取锁之后，内层递归函数仍然能获取锁，在
 * 同一个线程在外层方法获取锁的时候，在进入内层方法会自动获取锁
 * 也就是说线程可以进入任何一个他已经拥有锁的同步代码块。
 * 可重入锁就是，在一个method1方法中加入一把锁，方法2也加锁了，那么他们拥有的是同一把锁
 * 也就是说我们只需要进入method1后，那么它也能直接进入method2方法，因为他们所拥有的锁，是同一把。
 * ReentrantLock / Synchronized 就是一个典型的可重入锁
 * 可重入锁的最大作用就是避免死锁
 * <p>
 * 4 自旋锁：是指当前尝试获取锁 的线程不会立即阻塞，而且采用循环的方式去尝试获取锁。
 * 好处：减少线程上下文切换的消耗
 * 缺点：循环消耗cpu
 * <p>
 * 5 独占锁（写锁）：该锁只能被一个线程持有 ReentrantLock / Synchronized 就是一个典型的独占锁
 * <p>
 * 6 共享锁（读锁）：该锁可以被多个线程持有
 *
 * <>
 *     ReentrantLock VS Synchronized 的区别
        1）synchronized属于JVM层面，属于java的关键字
​               monitorenter（底层是通过monitor对象来完成，其实wait/notify等方法也依赖于monitor对象 只能在同步块或者方法中才能调用 wait/ notify等方法）
         Lock是具体类（java.util.concurrent.locks.Lock）是api层面的锁
        2）使用方法：
            synchronized：不需要用户去手动释放锁，当synchronized代码执行后，系统会自动让线程释放对锁的占用
            ReentrantLock：则需要用户去手动释放锁，若没有主动释放锁，就有可能出现死锁的现象，需要lock() 和 unlock() 配置try catch语句来完成
        3）等待是否中断
            synchronized：不可中断，除非抛出异常或者正常运行完成
            ReentrantLock：可中断，可以设置超时方法
            设置超时方法，trylock(long timeout, TimeUnit unit)
            lockInterrupible() 放代码块中，调用interrupt() 方法可以中断
        4）加锁是否公平
            synchronized：非公平锁
            ReentrantLock：默认非公平锁，构造函数可以传递boolean值，true为公平锁，false为非公平锁
        5）锁绑定多个条件Condition
            synchronized：没有，要么随机，要么全部唤醒
            ReentrantLock：用来实现分组唤醒需要唤醒的线程，可以精确唤醒，而不是像synchronized那样，要么随机，要么全部唤醒
 * </>
 *
 *
 * @Author: haoxd
 * @Version: 1.0
 */
public class LockDemo {

    static AtomicReference<Thread> threadAtomicReference = new AtomicReference<>();


    public static void main(String[] args) {

        readWriteLock();


    }

    /**
     * synchronized的可重入锁示例
     */
    private static void synchronizedLock() throws InterruptedException {
        Phone phone = new Phone();
        IntStream.rangeClosed(1, 5).forEach(i -> {
            new Thread(() -> {
                try {
                    phone.sendSMS();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "phone" + i).start();
        });

    }

    /**
     * ReentrantLock的可重入锁示例
     */
    private static void reentrantLock() {
        SMS sms = new SMS();

        IntStream.rangeClosed(1, 5).forEach(i -> {
            new Thread(sms, "sms" + i).start();
        });

    }

    /**
     * 自旋锁示例
     */
    private static void spin() {

        IntStream.rangeClosed(1, 3).forEach(i -> {

            String name = "ABC" + i;

            new Thread(() -> {
                System.out.println(name + " 准备執行代碼邏輯");

                spinLock();
                System.out.println(name + " 執行代碼邏輯");

                try {
                    TimeUnit.SECONDS.sleep(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + " 執行代碼邏輯完毕");

                unSpinLock();

            }, name).start();
        });


    }

    /**
     * 自旋锁加鎖示例
     */
    private static void spinLock() {

        Thread thread = Thread.currentThread();

        while (!threadAtomicReference.compareAndSet(null, thread)) {


        }
        System.out.println(thread.getName() + " 我得到了锁");

    }

    /**
     * 自旋锁解鎖示例
     */
    private static void unSpinLock() {

        Thread thread = Thread.currentThread();

        threadAtomicReference.compareAndSet(thread, null);
        System.out.println(thread.getName() + " 我释放了锁");

    }

    /**
     * 读写锁 多线程情况下：读-写互斥、写-读互斥、写-写互斥、读-读共享
     * <p>
     * 写操作：原子+独占，整个过程必须是完整的，中间不可被分割，或者打断
     * 读操作：
     **/
    private static void readWriteLock() {

        Cache cache = new Cache();

        for (int i = 1; i < 5; i++) {

            final int t = i;

            new Thread(() -> {
                cache.get(t + "");
            }, "readWriteLock: " + i).start();
        }
        for (int i = 1; i < 5; i++) {

            final int t = i;

            new Thread(() -> {
                cache.set(t + "", t + "");
            }, "readWriteLock: " + i).start();
        }


    }


}

class Cache {

    private volatile Map<String, String> cache = new HashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void set(String k, String v) {

        lock.writeLock().lock();
        try {

            System.out.println("正在写入：" + k);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cache.put(k, v);
            System.out.println("写入完成：" + k);

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            lock.writeLock().unlock();
        }


    }

    public String get(String k) {
        lock.readLock().lock();
        try {
            System.out.println("正在读取：" + k);
            String value = cache.get(k);
            System.out.println("读取完成：" + value);
            return value;
        } finally {
            lock.readLock().unlock();
        }


    }

    public void clear() {
        cache.clear();
    }
}

/**
 * 资源类
 */
class SMS implements Runnable {

    Lock lock = new ReentrantLock();

    /**
     * set进去的时候，就加锁，调用set方法的时候，能否访问另外一个加锁的set方法
     */
    public void getLock() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t get Lock");
            setLock();
        } finally {
            lock.unlock();

        }
    }

    public void setLock() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t set Lock");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        getLock();
    }
}

class Phone {

    /**
     * 发送短信
     *
     * @throws Exception
     */
    public synchronized void sendSMS() throws Exception {
        System.out.println(Thread.currentThread().getName() + "\t invoked sendSMS()");

        // 在同步方法中，调用另外一个同步方法
        sendEmail();
    }

    /**
     * 发邮件
     *
     * @throws Exception
     */
    public synchronized void sendEmail() throws Exception {
        System.out.println(Thread.currentThread().getName() + "\t invoked sendEmail()");
    }
}