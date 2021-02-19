package com.bread.coalquality.mvc.controller.ProducerConsumer.v2;

import java.util.stream.IntStream;

/**
 * @Description: 测试类
 * @Author: haoxd
 * @Version: 1.0
 */
public class Test {


    public static void main(String[] args) {

        DataBuffer dataBuffer = new DataBuffer(10);

        IntStream.rangeClosed(0, 20).forEach(i -> {
            new Thread(new Producer(dataBuffer, "ABC" + i), "Producer" + i).start();
        });


        IntStream.rangeClosed(0, 10).forEach(i -> {
            new Thread(new Consumer(dataBuffer), "Consumer" + i).start();
        });

        if (Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println(dataBuffer.toString());
    }


}
