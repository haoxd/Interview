package com.bread.coalquality.mvc.controller.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Description:循环屏障
 * @Author: haoxd
 * @Version: 1.0
 */
public class CyclicBarrierDemo {





    public static void main(String[] args) {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->System.out.println("BOOS開始開會"));

        for (int i = 1; i < 8; i++) {
            final Integer tempInt = i;
            new Thread(() -> {
                System.out.println("第"+tempInt+"位CEO到處");

                try {
                    // 先到的被阻塞，等全部线程完成后BOSS，才能执行方法
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }
    }


}
