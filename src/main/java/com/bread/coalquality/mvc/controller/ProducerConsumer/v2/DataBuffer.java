package com.bread.coalquality.mvc.controller.ProducerConsumer.v2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * 例如，假设我们有一个有界缓冲区，它支持put和take方法。
 * 如果尝试在空缓冲区上进行take ，则线程将阻塞，直到有可用项为止。
 * 如果尝试在完整的缓冲区上进行put ，则线程将阻塞，直到有可用空间为止。
 * 我们希望继续等待put线程，并在单独的等待集中take线程，以便我们可以使用仅在缓冲区中的项目或空间可用时才通知单个线程的优化。
 * 这可以使用两个Condition实例来实现。
 * i++是先赋值，然后再自增；++i是先自增，后赋值。
 * <p>
 * <p>
 * 当前   DataBuffer 当中 一个object[] 的初始大小是10,也可以构造指定大小的DataBuffer
 * <p>
 * 多线程下操作的三要素
 * 1,线程 操作 资源类
 * 2,判断 干活 通知
 * 3,防止虚假唤醒机制
 * <p>
 * </>
 **/
public class DataBuffer {

    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();//是否满的条件
    final Condition notEmpty = lock.newCondition();//是否空的条件

    final Object[] items;

    int putptr, takeptr, count;


    public DataBuffer(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();
        this.items = new Object[capacity];
    }

    public DataBuffer() {
        this.items = new Object[10];
    }


    /**
     * 获取当前DataBuffer的数组数据
     */
    public Object[] get() {
        return items;
    }

    /**
     * 往当前DataBuffer当中存放数据，如果当中DataBuffer的长度和存放的总数是一直的，put线程将阻塞
     *
     * @param x Object
     */
    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            // 如果 当前数据的长度等于put的总数，则 通知put线程进行阻塞等待，
            while (count == items.length) {
                System.out.println(Thread.currentThread().getName() + "线程：put操作需要进行等待，当前DataBuffer长度：" + items.length + "," + "当前DataBuffer总数：" + count);
                notFull.await();
            }
            System.out.println(Thread.currentThread().getName() + "线程：开始put，当前DataBuffer长度：" + items.length + "," + "当前DataBuffer总数：" + count);
            items[putptr] = x;
            if (++putptr == items.length) {
                putptr = 0;
            }
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 往当前DataBuffer取数据，如果当前长度为0 ，take线程将阻塞，取出后设置为空
     */
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            // 如果 当前数据的长度等于0，则 通知take线程进行阻塞等待，
            while (count == 0) {
                System.out.println(Thread.currentThread().getName() + "线程：take操作线程需要进行等待，当前DataBuffer长度：" + items.length + "," + "当前DataBuffer总数：" + count);
                notEmpty.await();
            }
            System.out.println(Thread.currentThread().getName() + "线程：开始take，当前DataBuffer长度：" + items.length + "," + "当前DataBuffer总数：" + count);

            Object x = items[takeptr];
            items[takeptr] = null;
            if (++takeptr == items.length) {
                takeptr = 0;
            }
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Stream.of(items).forEach(x -> stringBuilder.append(x + ","));
        return stringBuilder.toString();
    }
}
