package com.bread.coalquality.mvc.controller.ProducerConsumer.v3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 我的队列,
 **/
@Slf4j
public class MyQueue<T> {


    //默认开启，进行生产消费,这里用到了volatile是为了保持数据的可见性，也就是当TLAG修改时，要马上通知其它线程进行修改
    private volatile boolean FLAG = true;

    //使用原子包装类
    private AtomicInteger atomicInteger = new AtomicInteger();

    private final BlockingQueue<T> blockingQueue;

    public MyQueue(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
        log.info("MyQueue构造了:" + blockingQueue.getClass().getName() + "队列");
    }


    /**
     * 生产
     *
     * @throws Exception
     */
    public void myProd() throws Exception {

        String data;
        boolean retValue;
        // 多线程环境的判断，一定要使用while进行，防止出现虚假唤醒
        // 当FLAG为true的时候，开始生产
        while (FLAG) {
            data = atomicInteger.incrementAndGet() + "";

            // 2秒存入1个data
            retValue = blockingQueue.offer((T) data, 2L, TimeUnit.SECONDS);
            if (retValue) {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列:" + data + "成功");
            } else {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列:" + data + "失败");
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(Thread.currentThread().getName() + "\t 停止生产，表示FLAG=false，生产介绍");
    }


    /**
     * 消费
     *
     * @throws Exception
     */
    public void myConsumer() throws Exception {
        T retValue;
        // 多线程环境的判断，一定要使用while进行，防止出现虚假唤醒
        // 当FLAG为true的时候，开始生产
        while (FLAG) {
            // 2秒存入1个data
            retValue = blockingQueue.poll(2L, TimeUnit.SECONDS);
            if (retValue != null && retValue != "") {
                System.out.println(Thread.currentThread().getName() + "\t 消费队列:" + retValue + "成功");
            } else {
                FLAG = false;
                System.out.println(Thread.currentThread().getName() + "\t 消费失败，队列中已为空，退出");
                // 退出消费队列
                return;
            }
        }
    }

    public void stop() {
        this.FLAG=false;
    }
}