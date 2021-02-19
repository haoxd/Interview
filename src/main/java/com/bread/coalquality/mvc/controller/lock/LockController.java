package com.bread.coalquality.mvc.controller.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @Description:锁的一些问题
 * 加锁前要清楚锁和被保护的对象是不是一个层面的
 *  除了没有分析清线程、业务逻辑和锁三者之间的关系随意添加无效的方法锁外，还有一种比较常见的错误是，没有理清楚锁和要保护的对象是否是一个层面的。
 * 我们知道静态字段属于类，类级别的锁才能保护；而非静态字段属于类实例，实例级别的锁就可以保护。
 *
 * 加锁要考虑锁的粒度和场景问题
 * 在方法上加 synchronized 关键字实现加锁确实简单，也因此我曾看到一些业务代码中几乎所有方法都加了 synchronized，但这种滥用 synchronized 的做法：
 * 一是，没必要。通常情况下 60% 的业务代码是三层架构，数据经过无状态的 Controller、Service、Repository 流转到数据库，没必要使用 synchronized 来保护什么数据。
 * 二是，可能会极大地降低性能。使用 Spring 框架时，默认情况下 Controller、Service、Repository 是单例的，加上 synchronized 会导致整个程序几乎就只能支持单线程，造成极大的性能问题。
 * 即使我们确实有一些共享资源需要保护，也要尽可能降低锁的粒度，仅对必要的代码块甚至是需要保护的资源本身加锁。
 *
 *
 * 如果精细化考虑了锁应用范围后，性能还无法满足需求的话，我们就要考虑另一个维度的粒度问题了，
 * 即：区分读写场景以及资源的访问冲突，考虑使用悲观方式的锁还是乐观方式的锁。
 * 一般业务代码中，很少需要进一步考虑这两种更细粒度的锁，所以我只和你分享几个大概的结论，你可以根据自己的需求来考虑是否有必要进一步优化：
 * 对于读写比例差异明显的场景，考虑使用 ReentrantReadWriteLock 细化区分读写锁，来提高性能。
 * 如果你的 JDK 版本高于 1.8、共享资源的冲突概率也没那么大的话，考虑使用 StampedLock 的乐观读的特性，进一步提高性能。
 * JDK 里 ReentrantLock 和 ReentrantReadWriteLock 都提供了公平锁的版本，在没有明确需求的情况下不要轻易开启公平锁特性，在任务很轻的情况下开启公平锁可能会让性能下降上百倍。
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
@RequestMapping("/lock")
@RestController
public class LockController {


    private List<Integer> data = new ArrayList<>();

    //不涉及共享资源的慢方法
    private void slow() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    //错误的加锁方法
    @GetMapping("wrong")
    public int wrong() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            //加锁粒度太粗了
            synchronized (this) {
                slow();
                data.add(i);
            }
        });
        log.info("took:{}", System.currentTimeMillis() - begin);
        return data.size();
    }

    //正确的加锁方法
    @GetMapping("right")
    public int right() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            slow();
            //只对List加锁
            synchronized (data) {
                data.add(i);
            }
        });
        log.info("took:{}", System.currentTimeMillis() - begin);
        return data.size();
    }

    public static void main(String[] args) {
        LockController lockController = new LockController();
        lockController.run();
    }

    public  void run() {

        Interesting interesting = new Interesting();
        new Thread(() -> interesting.add()).start();
        new Thread(() -> interesting.compare()).start();

    }





    public class Interesting {

        volatile int a = 1;
        volatile int b = 1;

        public synchronized void add() {
            log.info("add start");
            for (int i = 0; i < 10000; i++) {
                a++;
                b++;
            }
            log.info("add done");
        }

        public synchronized void compare() {
            log.info("compare start");
            for (int i = 0; i < 10000; i++) {
                //a始终等于b吗？
                if (a < b) {
                    log.info("a:{},b:{},{}", a, b, a > b);
                    //最后的a>b应该始终是false吗？
                }else if(a>b){
                    log.info("a:{},b:{},{}", a, b, a > b);
                }
            }
            log.info("compare done");
        }
    }
}
