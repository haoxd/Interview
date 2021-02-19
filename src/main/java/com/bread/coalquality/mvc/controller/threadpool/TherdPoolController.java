package com.bread.coalquality.mvc.controller.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Description:
 * 至此，我们可以总结出线程池默认的工作行为：
 * 不会初始化 corePoolSize 个线程，有任务来了才创建工作线程；
 * 当核心线程满了之后不会立即扩容线程池，而是把任务堆积到工作队列中；当工作队列满了后扩容线程池，一直到线程个数达到 maximumPoolSize 为止；
 * 如果队列已满且达到了最大线程后还有任务进来，按照拒绝策略处理；当线程数大于核心线程数时，线程等待 keepAliveTime 后还是没有任务需要处理的话，收缩线程到核心线程数。
 * 了解这个策略，有助于我们根据实际的容量规划需求，为线程池设置合适的初始化参数。
 * 当然，我们也可以通过一些手段来改变这些默认工作行为，比如：声明线程池后立即调用 prestartAllCoreThreads 方法，来启动所有核心线程；传入 true 给 allowCoreThreadTimeOut 方法，来让线程池在空闲的时候同样回收核心线程。
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
@RequestMapping("/therd_pool")
@RestController
public  class TherdPoolController {

    @GetMapping("oom1")
    public void oom1() throws InterruptedException {

        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        //打印线程池的信息，稍后我会解释这段代码
        printStats(threadPool);
        for (int i = 0; i < 100000000; i++) {
            threadPool.execute(() -> {
                String payload = IntStream.rangeClosed(1, 1000000)
                        .mapToObj(a -> "1")
                        .collect(Collectors.joining("")) + UUID.randomUUID().toString();
                try {
                    TimeUnit.HOURS.sleep(1);
                } catch (InterruptedException e) {
                }
                log.info(payload);
            });
        }

        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }

    /***
     * 首先，自定义一个线程池。
     * 这个线程池具有 2 个核心线程、5 个最大线程、使用容量为 10 的 ArrayBlockingQueue 阻塞队列作为工作队列，
     * 使用默认的 AbortPolicy 拒绝策略，也就是任务添加到线程池失败会抛出 RejectedExecutionException。
     * 此外，我们借助了 google 类库的 ThreadFactoryBuilder 方法来构造一个线程工厂，实现线程池线程的自定义命名。
     * 然后，我们写一段测试代码来观察线程池管理线程的策略。测试代码的逻辑为，每次间隔 1 秒向线程池提交任务，循环 20 次，每个任务需要 10 秒才能执行完成
     * ***/
    @GetMapping("right")
    public int right() throws InterruptedException {
        //使用一个计数器跟踪完成的任务数
        AtomicInteger atomicInteger = new AtomicInteger();
        //创建一个具有2个核心线程、5个最大线程，使用容量为10的ArrayBlockingQueue阻塞队列作为工作队列的线程池，使用默认的AbortPolicy拒绝策略
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2, 5,
                5, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                new ThreadFactoryBuilder().setDaemon(true).setNameFormat("demo-threadpool-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());

        printStats(threadPool);
        //每隔1秒提交一次，一共提交20次任务
        IntStream.rangeClosed(1, 20).forEach(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int id = atomicInteger.incrementAndGet();
            try {
                threadPool.submit(() -> {
                    log.info("{} started", id);
                    //每个任务耗时10秒
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    log.info("{} finished", id);
                });
            } catch (Exception ex) {
                //提交出现异常的话，打印出错信息并为计数器减一
                log.error("error submitting task {}", id, ex);
                atomicInteger.decrementAndGet();
            }
        });

        TimeUnit.SECONDS.sleep(60);
        return atomicInteger.intValue();
    }


    public static void printStats(ThreadPoolExecutor threadPool) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("=========================");
            log.info("Pool Size: {}", threadPool.getPoolSize());
            log.info("Active Threads: {}", threadPool.getActiveCount());
            log.info("Number of Tasks Completed: {}", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());

            log.info("=========================");
        }, 0, 1, TimeUnit.SECONDS);
    }

}
