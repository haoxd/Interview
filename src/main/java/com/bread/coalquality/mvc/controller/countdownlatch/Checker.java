package com.bread.coalquality.mvc.controller.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * 第一个是启动信号，可防止任何工人继续前进，直到驾驶员为他们做好准备为止。
 * 第二个是完成信号，允许驾驶员等到所有工人都完成为止。
 **/
public class Checker {


    private static int N = 10;


    public static void main(String[] args) throws InterruptedException {

        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(N);

        for (int i = 0; i < N; ++i) {

            new Thread(new Worker(startSignal, doneSignal)).start();
        }


        doSomethingCheck(startSignal);


        doSomethingElse(doneSignal);
    }

    private static void doSomethingCheck(CountDownLatch startSignal) {
        System.out.println("检查者准备要开始检查了");
        startSignal.countDown();
        System.out.println("检查者已经做好准备");
    }

    private static void doSomethingElse(CountDownLatch doneSignal) throws InterruptedException {
        doneSignal.await();
        System.out.println("检查者开始检查了");
    }
}