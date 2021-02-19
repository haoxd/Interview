package com.bread.coalquality.mvc.controller.aqs;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: AbstractQueuedSynchronizer : 抽象队列同步器
 * 是用来构建锁或者其他同步器组件的重量级基础框架是整个juc的基础
 * 通过内置的fifo队列来完成资源获取线程的排队工作，并且通过一个int类型
 * 的变量表示线程持有锁的标记(变量+队列)
 *
 * <p>
 * AQS 使用一个volatile修饰的int类型的成员变量来表示同步状态
 * 通过设置fifo队列来完成资源获取的排队工资，将每一个要抢占资源的线程封装成一个个
 * node节点来实现锁的分配，通过CAS完成对state值的修改
 * </>
 *
 * <p> 参数说明
 * state   ：AQS 同步状态 0可以直接拿到资源进行 ,1或者大于1 需要进行AQS进行排队等待
 * CLH ：双向队列，尾部入队，头部出队
 * </>
 * <p> 内部node 说明
 * waitstatus : 0 -初始值 ，
 *              1-表示线程获取锁的请求取消了
 *              -2 标识节点在的部分带队列当中，节点线程等待唤醒
 *              -3 当前线程处在共享情况下才会使用
 *              -1 标识改node当中的线程已经准备好了，等待资源释放
 * CLH ：双向队列，尾部入队，头部出队
 * </>
 * <p> AQS 队列内部
 * 第一个节点为空节点
 * 第二个节点才是真正的线程节点
 * </>
 * <p> 源码分析（非公平锁分析）
 * ReentrantLock#lock 方法底层调用 sync#lock方法 （sync是一个内部类 继承 AQS）
 * NonfairSync # lock 方法 首先CAS进行尝试获取锁，如果CAS成功 则拿到锁，否则 执行  acquire(1) 方法
 *
 *  acquire 三步走-》{
 *
 *      第一步 通过至少调用一次tryAcquire 方法 ，并在成功后返回。 否则，线程将排队，并可能反复阻塞和解除阻塞，并调用tryAcquire直到成功。
 *      第二步  tryAcquire 返回 FALSE   执行 addWaiter(Node.EXCLUSIVE), arg) 方法 将当前的线程封装成 AQS 下的node 对象  ，进入队列俩步走->{
 *
 *              第一步 -》如果当前队列不为空，此时将当前node节点放入队列尾部
 *              第二步-》 如果当前队列为空 ，执行队列初始化 enq方法会初始化一个空的node 节点放入队列头部，然后再将排队的线程node节点作为 头部node节点的next 节点插入
 *
 *
 *      }
 *      第三步 acquireQueued   -》判断当前节点的前节点是否是头节点 -》{
 *
 *          是-》说明他是下一个获取锁的线程，调用一次tryAcquire 尝试获取锁，如果获取到，则重新维护队列的引用，否则 park 继续阻塞
 *          否-》判断当前节点的前一个节点的waitstatus状态是否是  SIGNAL（标识改node当中的线程已经准备好了，等待资源释放） 状态-》{
 *
 *              如果 ==0 设置为SIGNAL
 *              如果》0 表示前一个节点 已经取消了，将前一个节点移除队列。重新维护队列的关系
 *
 *              ### 由于是自旋上面的逻辑重新执行一遍
 *
 *          }
 *
 *      }
 *  }
 *
 * </>
 * @Author: haoxd
 * @Version: 1.0
 */
public class AQSDemo {


    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {

    }
}
