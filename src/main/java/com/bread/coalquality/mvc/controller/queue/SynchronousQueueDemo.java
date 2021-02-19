package com.bread.coalquality.mvc.controller.queue;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 不存储元素的阻塞队列，也即单个元素的队列生产一个，消费一个，不存储元素，不消费不生产
 * @Author: haoxd
 * @Version: 1.0
 */
public class SynchronousQueueDemo {

    public static void main(String[] args) {
        BlockingQueue<String> queue = new SynchronousQueue<>();


        new Thread(() -> {
            try {
                System.out.println("put 1");
                queue.put("1");


                System.out.println("put 2");
                queue.put("2");

                System.out.println("put 3");
                queue.put("3");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "AAA").start();


        new Thread(() -> {

            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(queue.take());

                TimeUnit.SECONDS.sleep(5);
                System.out.println(queue.take());

                TimeUnit.SECONDS.sleep(5);
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "BBB").start();

    }
}
