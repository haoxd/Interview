package com.bread.coalquality.mvc.controller.semaphore;

import java.util.concurrent.Semaphore;

/**
 * @Description:
 * 信号量主要用于两个目的
一个是用于共享资源的互斥使用
另一个用于并发线程数的控制
 * @Author: haoxd
 * @Version: 1.0
 */
public class SemaPhoreDemo {


    public static void main(String[] args) {
        Semaphore semaPhore = new Semaphore(3);
    }

}
