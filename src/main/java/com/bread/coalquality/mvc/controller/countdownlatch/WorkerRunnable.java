package com.bread.coalquality.mvc.controller.countdownlatch;

import java.util.concurrent.CountDownLatch;


public class WorkerRunnable implements Runnable {
    private final CountDownLatch doneSignal;
    private final int i;

    WorkerRunnable(CountDownLatch doneSignal, int i) {
        this.doneSignal = doneSignal;
        this.i = i;
    }

    public void run() {
        doWork(i);
        doneSignal.countDown();

    }

    private void doWork(int i) {

        System.out.println(i);
    }


}

