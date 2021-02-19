package com.bread.coalquality.mvc.controller.aqs;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: haoxd
 * @Version: 1.0
 * <p>
 * <p>
 * synchronized
 * wait
 * notify
 * <p>
 * 必须在 synchronized 代码块内部执行 ，必选先wait 在 notify 否则程序唤醒等待不生效
 * 必须先等待后唤醒，线程才可以被唤醒
 * 线程想要获得锁并持有锁，必须在锁块当中
 * <p>
 * </>
 * <p>
 * lock
 * await
 * signal
 * </>
 * <p>
 * <p>
 * LockSupport
 * park
 * unpark
 *
 * 1 无锁块要求
 * 2 LockSupport 底层 unsale类  -》 native 方法
 *
 *  park 许可证变为 0
 *  unpark 许可证变 1
 *
 * 许可证的累加上限是1 如果一个线程多次调用park 方法，另一个线程调用多次和调用一次的效果是一样的
 * 只会增加一个凭证，而调用多次是需要获取多个凭证，需要多个线程unpark才可以唤醒
 *
 * <p>
 * </>
 */
public class LockSupportDemo {

    private static Object synLock = new Object();

    private static Lock lock = new ReentrantLock();

    private static Condition condition = lock.newCondition();


    /**
     * synchronized
     * wait
     * notify
     */
    private static void test1() {


        new Thread(() -> {

            synchronized (synLock) {


                System.out.println(Thread.currentThread().getName() + "执行");
                try {
                    synLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("123");

            }

        }, "T1").start();

        new Thread(() -> {

            synchronized (synLock) {


                System.out.println(Thread.currentThread().getName() + "执行");

                synLock.notify();


            }

        }, "T2").start();
    }

    /**
     * ReentrantLock
     * await
     * signal
     **/
    public static void test2() {

        new Thread(() -> {

            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "执行");
                condition.await();
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                lock.unlock();
            }


        }, "T1").start();

        new Thread(() -> {

            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "执行");
                condition.signal();
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                lock.unlock();
            }


        }, "T2").start();

    }


    public static void test3(){


        Thread T1 = new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "执行");

            LockSupport.park();
            LockSupport.park();

            System.out.println(Thread.currentThread().getName() + "唤醒");

        },"T1");

        T1.start();


        Thread T2 = new Thread(()->{

            System.out.println(Thread.currentThread().getName() + "执行");
            LockSupport.unpark(T1);


        },"T2");
        T2.start();

        Thread T3 = new Thread(()->{

            System.out.println(Thread.currentThread().getName() + "执行");
            LockSupport.unpark(T1);


        },"T3");

        T3.start();


    }


    public static void main(String[] args) {


        test3();
    }


}
