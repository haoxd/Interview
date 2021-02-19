package com.bread.coalquality.mvc.controller.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: cas:比较并交换,原子地将该值设置为给定的更新值如果当前值{@code ==}期望的值。 compareAndSwap
 *                    是cpu的并发原语，这个过程是原子的，是一种完全依赖硬件的功能，
 *                    也就是说cas是cpu的原子指令，不会造成数据的不一致性问题。
 *
 *                 unsafe: 是cas的核心类，由于java无法直接操作底层系统，需要通过本地（native）方法来进行访问，
 *                          unsafe 是java基于该类可以直接操作底层内存的数据，内部的方法操作可以像c当中的指针一样
 *                          直接操作内存。java当中的cas操作的执行都是依赖unsafe类的。
 *
 *                 CAS 缺点：
 *                      1 有个do while  循环时间长，开销大
 *                      2 只能保证一个共享变量的原子操作
 *                      3 引发ABA问题
 *
 *
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
public class CASDemo {

    public static void main(String[] args) {

        AtomicInteger atomicInteger = new AtomicInteger(5);

        atomicInteger.compareAndSet(1,6);

        log.info(""+atomicInteger.get());
    }
}
