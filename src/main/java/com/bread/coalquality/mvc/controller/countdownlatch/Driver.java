package com.bread.coalquality.mvc.controller.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Driver {

    public static void main(String[] args) throws InterruptedException {

        int N = 5;
        CountDownLatch doneSignal = new CountDownLatch(N);
        Executor e = Executors.newSingleThreadScheduledExecutor();
        for (int i = 0; i < N; ++i) // create and start threads
        {
            e.execute(new WorkerRunnable(doneSignal, i));
        }
        doneSignal.await();           // wait for all to finish
    }

}