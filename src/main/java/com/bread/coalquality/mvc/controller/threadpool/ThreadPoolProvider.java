package com.bread.coalquality.mvc.controller.threadpool;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * @Auther haoxd
 * @description: 系统线程池
 * <p> 为什么用线程池
 * 线程池做的主要工作就是控制运行的线程的数量，处理过程中，将任务放入到队列中，然后线程创建后，启动这些任务，
 * 如果线程数量超过了最大数量的线程排队等候，等其它线程执行完毕，再从队列中取出任务来执行。
 * 它的主要特点为：线程复用、控制最大并发数、管理线程
 * 线程池中的任务是放入到阻塞队列中的
 * </>
 * <p>线程池的好处
 * 多核处理的好处是：省略的上下文的切换开销
 * 因此使用多线程有下列的好处
 * 1,降低资源消耗。通过重复利用已创建的线程，降低线程创建和销毁造成的消耗
 * 2,提高响应速度。当任务到达时，任务可以不需要等到线程创建就立即执行
 * 3,提高线程的可管理性。线程是稀缺资源，如果无线创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一的分配，调优和监控
 * </>
 * <p>下面我们一一介绍这些参数的意义，你可以把线程池类比为一个项目组，而线程就是项目组的成员。
 * corePoolSize：表示线程池保有的最小线程数。有些项目很闲，但是也不能把人都撤了，至少要留 corePoolSize 个人坚守阵地。
 * maximumPoolSize：表示线程池创建的最大线程数。当项目很忙时，就需要加人，但是也不能无限制地加，最多就加到 maximumPoolSize 个人。当项目闲下来时，就要撤人了，最多能撤到 corePoolSize 个人。
 * keepAliveTime & unit：上面提到项目根据忙闲来增减人员，那在编程世界里，如何定义忙和闲呢？很简单，一个线程如果在一段时间内，都没有执行任务，说明很闲，keepAliveTime 和 unit 就是用来定义这个“一段时间”的参数。也就是说，如果一个线程空闲了keepAliveTime & unit这么久，而且线程池的线程数大于 corePoolSize ，那么这个空闲的线程就要被回收了。
 * workQueue：工作队列，和上面示例代码的工作队列同义。
 * threadFactory：通过这个参数你可以自定义如何创建线程，例如你可以给线程指定一个有意义的名字。
 * handler：通过这个参数你可以自定义任务的拒绝策略。如果线程池中所有的线程都在忙碌，并且工作队列也满了（前提是工作队列是有界队列），那么此时提交任务，线程池就会拒绝接收。至于拒绝的策略，你可以通过 handler 这个参数来指定。
 * ThreadPoolExecutor 已经提供了以下 4 种策略。
 *
 * CallerRunsPolicy：提交任务的线程自己去执行该任务。
 * AbortPolicy：默认的拒绝策略，会 throws RejectedExecutionException。
 * DiscardPolicy：直接丢弃任务，没有任何异常抛出。
 * DiscardOldestPolicy：丢弃最老的任务，其实就是把最早进入工作队列的任务丢弃，然后把新任务加入到工作队列。
 * </>
 * <p>工作原理
 * 在创建了线程池后，等待提交过来的任务请求
 *  1 当调用execute()方法添加一个请求任务时，线程池会做出如下判断
 *  2 如果正在运行的线程池数量小于corePoolSize，那么马上创建线程运行这个任务
 *  3 如果正在运行的线程数量大于或等于corePoolSize，那么将这个任务放入队列
 *  4 如果这时候队列满了，并且正在运行的线程数量还小于maximumPoolSize，那么创建非核心线程运行这个任务；
 *  5 如果队列满了并且正在运行的线程数量大于或等于maximumPoolSize，那么线程池会启动饱和拒绝策略来执行
 *  6当一个线程完成任务时，它会从队列中取下一个任务来执行
 *  7当一个线程无事可做操作一定的时间(keepAliveTime)时，线程池会判断：
 *  8如果当前运行的线程数大于corePoolSize，那么这个线程就被停掉
 *  9所以线程池的所有任务完成后，它会最终收缩到corePoolSize的大小
 * </>
 * <p>如何合理配置线程池参数
 * 在创建了线程池后，等待提交过来的任务请求
 *  1 这个是根据具体业务来配置的，分为CPU密集型和IO密集型
 *  2 CPU密集型：CPU核数 + 1个线程数
 *  3 IO密集型：（1）CPU核数 / (1 - 阻塞系数) 阻塞系数在0.8 ~ 0.9左右 （2）
 * </>
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadPoolProvider {


    private static final int POOL_SIZE = ThreadUtil.getSuitableThreadCount();

    /**
     * 使用LinkedBlockingDeque 避免JVM初始化时就分配内存空间，造成内存浪费
     */
    private static final BlockingQueue<Runnable> QUEUE = new LinkedBlockingDeque<>(512);

    private static final RejectedExecutionHandler REJECT_HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

    private static final ThreadPoolThreadFactory THREAD_POOL_THREAD_FACTORY = new ThreadPoolThreadFactory();


    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE << 1,
            60, TimeUnit.SECONDS, QUEUE,THREAD_POOL_THREAD_FACTORY , REJECT_HANDLER);

    public static ThreadPoolExecutor getPool() {
        return POOL;
    }

    public static void destroyThreadPool() {
        log.info("销毁线程池");
        POOL.shutdown();
    }

    private static class ThreadRejectHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                TimeUnit.SECONDS.sleep(60*5);
            } catch (InterruptedException e) {
                log.error("线程休眠被中断");
            }

            executor.execute(r);
        }
    }

    private static class ThreadPoolThreadFactory implements ThreadFactory {


        /**
         *
         * thread.setDaemon(true);当为守护线程的时候,主方法结束,守护线程就会结束.
         *thread.setDaemon(false);当为即实线程的时候,主方法结束,即实线程不会结束
         * Constructs a new {@code Thread}.  Implementations may also initialize
         * priority, name, daemon status, {@code ThreadGroup}, etc.
         *
         * @param r a runnable to be executed by new thread instance
         * @return constructed thread, or {@code null} if the request to
         * create a thread is rejected
         */
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("com.bread.coalquality.mvc.controller.threadpool.ThreadPoolThreadFactory");
            return t;
        }
    }



    public static void main(String[] args) {
        ThreadPoolExecutor pool = ThreadPoolProvider.getPool();

        IntStream.rangeClosed(1, 200000).forEach(x -> {
            log.info("我是第" + x + "个任务,要开始了");
            pool.submit(() ->

                    LongStream.rangeClosed(1, 100).forEach(a -> {
                        log.info("我是第" + x + "个任务的第" + a + "子任务我的名字是：" + Thread.currentThread().getName());
                        try {
                            TimeUnit.SECONDS.sleep(x);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        log.info("我是第" + x + "个任务的第" + a + "子任务我的名字是：" + Thread.currentThread().getName() + "，我执行完毕了");
                    })

            );
            printStats(pool);
        });


    }


    private static void printStats(ThreadPoolExecutor threadPool) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("=========================");
            log.info("Pool Size: {}", threadPool.getPoolSize());
            log.info("Active Threads: {}", threadPool.getActiveCount());
            log.info("Number of Tasks Completed: {}", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());

            log.info("=========================");
        }, 0, 1, TimeUnit.MINUTES);
    }

    public class task implements Runnable {


        @Override
        public void run() {
            LongStream.rangeClosed(1, 100000).forEach(x -> {
                System.out.println(x);
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("睡眠");
            });
        }
    }
}
