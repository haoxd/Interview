package com.bread.coalquality.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

/**
 * @Description:LRUCache
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
@RequestMapping("/lru")
@RestController
public class LRUCacheController {


    public static void main(String[] args) {

        IntStream.rangeClosed(0, 5).forEach(x -> {
            IntStream.rangeClosed(0, 5).forEach(a -> {
                new Thread(() -> whiles(a)).start();
            });
        });

    }

    private static void whiles(int o) {
        while (o < 1) {
            System.out.println("我满足");
        }
        System.out.println("1111");
    }

    private static void ifs(int o) {
        if (o < 1) {
            System.out.println("我满足");
        }
        System.out.println("1111");
    }
}
