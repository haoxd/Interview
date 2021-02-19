package com.bread.coalquality.mvc.controller.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Description: ABA: 进行获取主内存值的时候，该内存值在我们写入主内存的时候，已经被修改了N次，但是最终又改成原来的值了
 * CAS只管开头和结尾，也就是头和尾是一样，那就修改成功，中间的这个过程，可能会被人修改过
 * <p>
 * 如何解决ABA
 * 1   AtomicStampedReference 时间戳原子引用，来这里应用于版本号的更新，也就是每次更新的时候，需要比较期望值和当前值，以及期望版本号和当前版本号
 * 2
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
public class ABADemo {


    static AtomicReference<Integer> integerAtomicReference = new AtomicReference<>(100);

    static AtomicStampedReference<Integer> integerAtomicStampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {
        atomicReference();
        atomicStampedReference();

    }


    /**
     * AtomicReference 原子应用的示例代码，会产生ABA问题
     * <p>
     * 跑俩个线程，T1线程进行产生ABA现象，T2线程进行获取
     **/
    private static void atomicReference() {


        new Thread(() -> {
            //先判断如果是100修改为101，然后在修改回去100 ，这就是ABA的现象
            integerAtomicReference.compareAndSet(100, 101);
            integerAtomicReference.compareAndSet(101, 100);
        }, "T1").start();


        new Thread(() -> {

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean b = integerAtomicReference.compareAndSet(100, 1024);
            log.info(b + "######" + integerAtomicReference.get());

        }, "T2").start();
    }

    /**
     * AtomicStampedReference 原子引用时间戳的示例代码，会产生ABA问题
     * <p>
     * 跑俩个线程，T1线程进行产生ABA现象，T2线程进行获取
     **/
    private static void atomicStampedReference() {


        new Thread(() -> {
            int version = integerAtomicStampedReference.getStamp();

            log.info("T3 第一次版本号:" + version);



            // 传入4个值，期望值，更新值，期望版本号，更新版本号
            integerAtomicStampedReference.compareAndSet(100, 101, integerAtomicStampedReference.getStamp(), integerAtomicStampedReference.getStamp()+1);

            log.info("T3 第二次版本号:" + integerAtomicStampedReference.getStamp());

            integerAtomicStampedReference.compareAndSet(101, 100, integerAtomicStampedReference.getStamp(), integerAtomicStampedReference.getStamp()+1);

            log.info("T3 第三次版本号:" + integerAtomicStampedReference.getStamp());



            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }, "T3").start();


        new Thread(() -> {

            // 获取版本号
            int stamp = integerAtomicStampedReference.getStamp();
            log.info("T4 第1次版本号:" + integerAtomicStampedReference.getStamp());

            // 暂停t4 3秒钟，保证t3线程也进行一次ABA问题
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean result = integerAtomicStampedReference.compareAndSet(100, 1024, stamp, stamp+1);

            log.info(Thread.currentThread().getName() + "\t 修改成功否：" + result + "\t 当前最新实际版本号：" + integerAtomicStampedReference.getStamp());

            log.info(Thread.currentThread().getName() + "\t 当前实际最新值" + integerAtomicStampedReference.getReference());




        }, "T4").start();
    }

}
