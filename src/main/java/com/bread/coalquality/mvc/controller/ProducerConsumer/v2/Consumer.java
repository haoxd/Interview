package com.bread.coalquality.mvc.controller.ProducerConsumer.v2;

/**
 * @Description: 定义一个消费者，用于消费DataBuffer
 * @Author: haoxd
 * @Version: 1.0
 */
public class Consumer implements Runnable {


    private DataBuffer dataBuffer;

    public Consumer(DataBuffer dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            dataBuffer.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
