package com.bread.coalquality.mvc.controller.queue;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description: BlockingQueue 阻塞队列，排队拥堵，首先它是一个队列
 * <p>
 * 当阻塞队列是空时，从队列中获取元素的操作将会被阻塞
 * </>
 * <p>
 * 当阻塞队列是满时，从队列中添加元素的操作将会被阻塞
 * </>
 * <p>
 * 也就是说 试图从空的阻塞队列中获取元素的线程将会被阻塞，直到其它线程往空的队列插入新的元素
 * 同理，试图往已经满的阻塞队列中添加新元素的线程，直到其它线程往满的队列中移除一个或多个元素，或者完全清空队列后，使队列重新变得空闲起来，并后续新增
 * <p>
 * BlockingQueue阻塞队列是属于一个接口，底下有七个实现类
 * <p>
 * 1 * ArrayBlockQueue：由数组结构组成的有界阻塞队列
 * 2 * LinkedBlockingQueue：由链表结构组成的有界（但是默认大小 Integer.MAX_VALUE(2147483647)）的阻塞队列有界，但是界限非常大，相当于无界，可以当成无界
 * 3   PriorityBlockQueue：支持优先级排序的无界阻塞队列
 * 4   DelayQueue：使用优先级队列实现的延迟无界阻塞队列
 * 5 * SynchronousQueue：不存储元素的阻塞队列，也即单个元素的队列生产一个，消费一个，不存储元素，不消费不生产
 * 6   LinkedTransferQueue：由链表结构组成的无界阻塞队列
 * 7   LinkedBlockingDeque：由链表结构组成的双向阻塞队列
 * </p>
 * @Author: haoxd
 * @Version: 1.0
 */
public class BlockingQueueDemo {


    public static void main(String[] args) {
        operBlockingTimeOut();
    }

    /**
     * 队列操作返回异常的操作
     * add()：如果队列满在继续add操作 抛出：java.lang.IllegalStateException: Queue full
     * remove()：如果队列空，继续 取元素 抛出 ：java.util.NoSuchElementException
     **/
    private static void operExpetion() {
        Queue<String> queue = new ArrayBlockingQueue<>(3);
        queue.add("a");
        queue.add("b");
        queue.add("c");


        queue.remove();
        queue.remove();
        queue.remove();
        queue.remove();

    }

    /**
     * 队列操作返回Boolean的操作
     * offer()：如果队列满在继续offer操作 返回：false
     * poll()：如果队列空，继续 取元素 返回：null
     **/
    private static void operBoolean() {
        Queue<String> queue = new ArrayBlockingQueue<>(3);

        System.out.println(queue.offer("a"));
        System.out.println(queue.offer("a"));
        System.out.println(queue.offer("a"));
        System.out.println(queue.offer("a"));

        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());

    }

    /**
     * 队列操作出現阻塞的方法
     * put()：如果队列满在继续put操作 ,隊列一直阻塞生產綫程直到可以put操作或者中断操作
     * take()：如果队列空，继续 take取元素 隊列一直阻塞消费綫程直到可以take操作或者中断操作
     **/
    private static void operBlocking() {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);

        try {
            queue.put("A");
            queue.put("B");
            queue.put("C");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            queue.take();
            queue.take();
            queue.take();
            queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 队列操作指定時間的方法
     * offer()：如果队列满在继续offer操作 ,使用offer插入的时候，需要指定时间，如果2秒还没有插入，那么就放弃插入
     * poll()：如果队列空，继续 poll取元素 使用poll取的时候，需要指定时间，如果2秒内取不出来，那么就返回null
     **/
    private static void operBlockingTimeOut() {

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        try {
            System.out.println(blockingQueue.offer("a", 2L, TimeUnit.SECONDS));
            System.out.println(blockingQueue.offer("b", 2L, TimeUnit.SECONDS));
            System.out.println(blockingQueue.offer("c", 2L, TimeUnit.SECONDS));
            System.out.println(blockingQueue.offer("d", 2L, TimeUnit.SECONDS));

            System.out.println(blockingQueue.poll(2L, TimeUnit.SECONDS));
            System.out.println(blockingQueue.poll(2L, TimeUnit.SECONDS));
            System.out.println(blockingQueue.poll(2L, TimeUnit.SECONDS));
            System.out.println(blockingQueue.poll(2L, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
