package com.bread.coalquality.mvc.controller;

import cn.hutool.core.math.MathUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

/**
 * @Description: ArrayList 线程不安全示例,add方法是线程不安全的
 * 1: 问题
 * java.util.ConcurrentModificationException
 * <p>
 * 2 原因:并发修改导致
 * <p>
 * 3 解決方法
 * 1 new Vector
 * 2 Collections.synchronizedList
 * 3 new CopyOnWriteArrayList 但因为其实现方式是，每次修改数据时都会复制一份数据出来，所以有明显的适用场景，即读多写少或者说希望无锁读的场景。
 * 4 规避问题
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
public class ArrayListThreadNoSafeDemo {

    public static void main(String[] args) {

        //Constructs an empty list with an initial capacity of ten.
        List<String> stringList = new ArrayList<>(10000);

        IntStream.rangeClosed(1, 10).forEach(i -> {

            new Thread(() -> {
                stringList.add(UUID.randomUUID().toString());
                System.out.println(stringList.toString());
            }, "T" + i).start();
        });

        IntStream.rangeClosed(1, 10000).forEach(i -> stringList.add(i + ""));


        IntStream.rangeClosed(1, 10).forEach(i -> {

            new Thread(() -> {

                IntStream.rangeClosed(1, 100).forEach(a -> {

                    new Thread(() -> {

                        System.out.println(stringList.get((int) (Math.random() * 10000)));
                    }, "T" + a).start();
                });
            }, "T" + i).start();
        });
    }
}
