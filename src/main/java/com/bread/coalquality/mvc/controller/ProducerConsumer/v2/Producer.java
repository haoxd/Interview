package com.bread.coalquality.mvc.controller.ProducerConsumer.v2;

/**
 * @Description: 定义一个生产者用于往DataBuffer当中放入值
 * @Author: haoxd
 * @Version: 1.0
 */
public class Producer implements Runnable {


    private DataBuffer dataBuffer;
    private String putValue;

    public Producer(DataBuffer dataBuffer, String putValue) {
        this.dataBuffer = dataBuffer;
        this.putValue = putValue;
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
            dataBuffer.put(putValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
