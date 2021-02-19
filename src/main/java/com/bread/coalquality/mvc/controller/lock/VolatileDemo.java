package com.bread.coalquality.mvc.controller.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

/**
 * @Description:Volatile 面试相关
 * Volatile ：是java虚拟机提供的轻量级的同步机制
 * 1 : 保证可见性
 * 2 : 不保证原子性
 * 3 : 禁止指令重排 :多线程环境下，由于线程交替执行，由于存在指令重排优化的存在，，俩个线程使用的变量能否保证一致性是无法确定的，结果无法预测。
 * JMM: java 内存模型
 * JMM定义了Java 虚拟机(JVM)在计算机内存(RAM)中的工作方式。
 * JVM是整个计算机虚拟模型，所以JMM是隶属于JVM的。
 * 从抽象的角度来看，JMM定义了线程和主内存之间的抽象关系：线程之间的共享变量存储在主内存（Main Memory）中，
 * 每个线程都有一个私有的本地内存（Local Memory），本地内存中存储了该线程以读/写共享变量的副本。
 * 本地内存是JMM的一个抽象概念，并不真实存在。它涵盖了缓存、写缓冲区、寄存器以及其他的硬件和编译器优化。
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
public class VolatileDemo {


    public static volatile String str = "abc";

    public static volatile int number = 0;

    public static AtomicInteger atomicNumber = new AtomicInteger();


    public static Object lock = new Object();


    public static void main(String[] args) {

        atomic();
        atomicNumber();

    }

    /**
     * 可见性代码示例
     **/
    public static void visibility() {

        new Thread(() -> {
            log.info(Thread.currentThread().getName() + ":我开始执行," + str);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            str = str + "abc";

            log.info(Thread.currentThread().getName() + ":我执行完事了," + str);

        }).start();

        while (StringUtils.equals(str, "abc")) {
            log.info(Thread.currentThread().getName() + ":" + str);
        }
        log.info(Thread.currentThread().getName() + ":" + str);

    }


    /**
     * 不保障原子性代码示例
     **/
    public static void atomic() {


        LongStream.rangeClosed(1, 20).forEach(i -> {

            new Thread(() -> {

                LongStream.rangeClosed(1, 1000).forEach(a -> {

                            number++;
                        }
                );

            }, i + "thread").start();

        });

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }

        log.info(number + "");
    }


    /**
     * 保障原子性代码示例
     **/
    public static void atomicNumber() {

        LongStream.rangeClosed(1, 20).forEach(i ->

                new Thread(() -> {

                    LongStream.rangeClosed(1, 1000).forEach(a -> {

                                atomicNumber.getAndIncrement();
                            }
                    );

                }, i + "thread").start()

        );

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }

        log.info(atomicNumber.get() + "");
    }


}
