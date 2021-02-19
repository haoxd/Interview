package com.bread.coalquality.mvc.controller.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {

    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;

    Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }

    public void run() {
        try {
            startSignal.await();
            doWork();
            doneSignal.countDown();
        } catch (InterruptedException ex) {

        }
    }

    void doWork() {
        System.out.println(this.hashCode() + "完成了工作");
    }
}
