package com.bread.coalquality.mvc.controller.threadpool.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Callable接口，是一种让线程执行完成后，能够返回结果的
 * @Author: haoxd
 * @Version: 1.0
 */
public class CallableWorker implements Callable<String> {

    private int time;

    CallableWorker(int time) {
        this.time = time;
    }


    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public String call() throws Exception {
        TimeUnit.SECONDS.sleep(time);
        System.out.println(Thread.currentThread().getName());
        return "1";
    }
}
