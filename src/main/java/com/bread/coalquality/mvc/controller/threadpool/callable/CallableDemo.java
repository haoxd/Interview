package com.bread.coalquality.mvc.controller.threadpool.callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Description: <p>
 * run方法和start方法的区别:
 * 1 run->方法当作普通方法的方式调用,程序还是要顺序执行，要等待run方法体执行完毕后，才可继续执行下面的代码； 程序中只有主线程——这一个线程， 其程序执行路径还是只有一条， 这样就没有达到写线程的目的。
 * 2 start->方法来启动线程，真正实现了多线程运行。
 * 这时无需等待run方法体代码执行完毕，可以直接继续执行下面的代码；通过调用Thread类的start()方法来启动一个线程，
 * 这时此线程是处于就绪状态， 并没有运行。
 * 然后通过此Thread类调用方法run()来完成其运行操作的， 这里方法run()称为线程体，它包含了要执行的这个线程的内容， Run方法运行结束， 此线程终止。然后CPU再调度其它线程。
 * 线程的run()方法是由java虚拟机直接调用的，
 * 如果我们没有启动线程（没有调用线程的start()方法）而是在应用代码中直接调用run()方法，
 * 那么这个线程的run()方法其实运行在当前线程（即run()方法的调用方所在的线程）之中，
 * 而不是运行在其自身的线程中，从而违背了创建线程的初衷；
 * </>
 * @Author: haoxd
 * @Version: 1.0
 */
public class CallableDemo {


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask<String> futureTask1 = new FutureTask<>(new CallableWorker(1));

        FutureTask<String> futureTask2 = new FutureTask<>(new CallableWorker(2));

        FutureTask<String> futureTask3 = new FutureTask<>(new CallableWorker(3));

        FutureTask<String> futureTask4 = new FutureTask<>(new CallableWorker(5));

        new Thread(futureTask4, "futureTask4").start();
        new Thread(futureTask1, "futureTask1").start();
        new Thread(futureTask2, "futureTask2").start();
        new Thread(futureTask3, "futureTask3").start();

        System.out.println("1111111111111111111111111111111111111111111111111111111111");

        String futureTask1str = futureTask1.get();
        String futureTask2str = futureTask2.get();
        String futureTask3str = futureTask3.get();
        String futureTask4str = futureTask4.get();
        System.out.println(futureTask1str+futureTask2str+futureTask3str+futureTask4str);

    }
}
