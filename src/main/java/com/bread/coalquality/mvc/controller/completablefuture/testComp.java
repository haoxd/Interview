package com.bread.coalquality.mvc.controller.completablefuture;

import com.bread.coalquality.mvc.controller.threadpool.ThreadPoolProvider;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: haoxd
 * @Version: 1.0
 */
public class testComp {

    public static void main(String[] args) {

        try {
            CompletableFuture.
                    supplyAsync(() -> {

                        return test1();
                    }, ThreadPoolProvider.getPool())

                    .thenApplyAsync(flag -> {
                        System.out.println("######:" + flag);

                        return test2() && flag;
                    }, ThreadPoolProvider.getPool())


                    .whenComplete((flag, expetion)-> {
                        System.out.println("$$$$$$$$:" + flag);
                        System.out.println("$$$$$$$$:" + expetion);
                        save(flag);
                    })
                    .exceptionally(ex -> {
                throw new RuntimeException(ex.getMessage());
            }).get();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void save(Boolean flag) {
    }




    public static boolean test1() {


        System.out.println("test1");


        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;

    }

    public static boolean test2() {


        System.out.println("test2");
        System.out.println(1/0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;

    }


}
