package com.bread.coalquality.mvc.controller.completablefuture;

import com.bread.coalquality.mvc.controller.threadpool.ThreadPoolProvider;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description:CompletableFutureDemo 异步任务
 * @Author: haoxd
 * @Version: 1.0
 */
public class CompletableFutureDemo {


    /**
     * 无返回值
     */
    public static void test1() {

        System.out.println("start");
        CompletableFuture completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(1);
            int a = 5;
            System.out.println(2);

        }, ThreadPoolProvider.getPool());
        System.out.println("end");
    }


    /**
     * 有返回值
     */
    public static void test2() throws ExecutionException, InterruptedException {

        System.out.println("start");
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(1);
            int a = 5;
            System.out.println(2);
            return a;

        }, ThreadPoolProvider.getPool());

        System.out.println(completableFuture.get());
        System.out.println("end");
    }


    /**
     * 完成進行回調執行,虽然可以处理异常但是没有办法进行修改返回数据
     */
    public static void test3() throws ExecutionException, InterruptedException {

        System.out.println("start");
        boolean completableFuture = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println(1);
                    System.out.println(2);
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }, ThreadPoolProvider.getPool())
                .whenComplete((result, exception) -> {
                    System.out.println("$$$$$$$$$$"+result);
                }).get();

        System.out.println("end");
    }


    /**
     * 完成進行回調執行 有异常，可以感知异常，同时返回默认值
     */
    public static void test4() throws ExecutionException, InterruptedException {

        System.out.println("start");
        CompletableFuture<Integer> completableFuture = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println(1);
                    int a = 5;
                    System.out.println(2);
                    return a;
                }, ThreadPoolProvider.getPool())
                .whenComplete((result, expetion) -> {
                    System.out.println(result * 10);
                    System.out.println(expetion);
                })
                .exceptionally(ex -> {
                    int a = 100;
                    return a * 100;
                });
        System.out.println(completableFuture.get());
        System.out.println("end");
    }


    /**
     * 完成進行回調執行 ，无论失败还是成功后的处理
     */
    public static void test5() throws ExecutionException, InterruptedException {

        System.out.println("start");
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(1);
            int a = 5;
            System.out.println(2);
            return a;

        }, ThreadPoolProvider.getPool()).handle((result, ex) -> {

            if (Objects.nonNull(result)) {
                return 100;
            }
            if (Objects.nonNull(ex)) {
                return 200;
            }
            return result;
        });

        System.out.println("end" + completableFuture.get());
    }

    /***
     * 多个线程串联,下个线程不感知上个线程的结果，无返回值
     *
     * **/
    public static void test6() throws ExecutionException, InterruptedException {

        CompletableFuture<Void> stringCompletableFuture = CompletableFuture.runAsync(() -> {

            System.out.println("1" + "启动");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, ThreadPoolProvider.getPool()).thenRunAsync(() -> {

            System.out.println("2" + "启动");
        }, ThreadPoolProvider.getPool());
    }

    /***
     * 多个线程串联,下个线程 获取上个线程的结果，无返回值
     *
     * **/
    public static void test7() throws ExecutionException, InterruptedException {

        CompletableFuture.supplyAsync(() -> {

            System.out.println("ACSSSSSSSSS" + "启动");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ABC";


        }, ThreadPoolProvider.getPool()).thenAcceptAsync(res -> {

            System.out.println(res + "ABC");

        }, ThreadPoolProvider.getPool()).get();
    }

    /***
     * 多个线程串联,下个线程 获取上个线程的结果，有返回值,是阻塞的
     *
     * **/
    public static void test8() throws ExecutionException, InterruptedException {

        CompletableFuture.supplyAsync(() -> {

            System.out.println("VVVVVVVVVVVVVVVVVVVV" + "启动");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ABC";

        }, ThreadPoolProvider.getPool()).thenApplyAsync(res -> {
            System.out.println("YYYYYYYYYYYYYYYYYYYYYY");
            return res + "CCCC";

        }, ThreadPoolProvider.getPool());


    }


    /***
     * 等待线程1和线程2 完成才 执行线程3，不会感知到上面俩个线程的返回结果
     *
     * **/
    public static void test9() throws ExecutionException, InterruptedException {


        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> {

            System.out.println("任务1启动");
            return "abc";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<String> stringCompletableFuture2 = CompletableFuture.supplyAsync(() -> {

            System.out.println("任务2启动");
            return "abc";
        }, ThreadPoolProvider.getPool());


        stringCompletableFuture1.runAfterBothAsync(stringCompletableFuture2, () -> {

            System.out.println("任务3启动");
        }, ThreadPoolProvider.getPool());

    }

    /***
     * 等待线程1和线程2 完成才 执行线程3，可以感知到上面俩个线程的返回结果
     *
     * **/
    public static void test10() throws ExecutionException, InterruptedException {


        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> {

            System.out.println("任务1启动");
            return "abc";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<String> stringCompletableFuture2 = CompletableFuture.supplyAsync(() -> {

            System.out.println("任务2启动");
            return "abc";
        }, ThreadPoolProvider.getPool());


        stringCompletableFuture1.thenAcceptBothAsync(stringCompletableFuture2, (re1, re2) -> {
            System.out.println("任务3启动" + re1 + re2);
        }, ThreadPoolProvider.getPool());

    }

    /***
     * 等待线程1和线程2 完成才 执行线程3，可以感知到上面俩个线程的返回结果,但是沒有返回值
     *
     * **/
    public static void test11() throws ExecutionException, InterruptedException {


        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> {

            System.out.println("任务1启动");
            return "abc";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<String> stringCompletableFuture2 = CompletableFuture.supplyAsync(() -> {

            System.out.println("任务2启动");
            return "abc";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<Void> voidCompletableFuture = stringCompletableFuture1.thenAcceptBothAsync(stringCompletableFuture2, (re1, re2) -> {
            System.out.println("任务3启动" + re1 + re2);
        }, ThreadPoolProvider.getPool());

    }


    /***
     * 等待线程1和线程2 完成才 执行线程3，可以感知到上面俩个线程的返回结果,有返回值
     *
     * **/
    public static void test12() throws ExecutionException, InterruptedException {


        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> {

            System.out.println("任务1启动");
            return "abc";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<String> stringCompletableFuture2 = CompletableFuture.supplyAsync(() -> {

            System.out.println("任务2启动");
            return "abc";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<String> stringCompletableFuture = stringCompletableFuture1.thenCombineAsync(stringCompletableFuture2, (re1, re2) -> {
            System.out.println("任务3启动" + re1 + re2);
            return re1 + re2;
        }, ThreadPoolProvider.getPool());

        System.out.println(stringCompletableFuture.get());

    }


    /***
     * 俩个任务，只要有一个完成，就执行下面的任务,不感知结果，自己也没有返回值
     *
     * **/
    public static void test13() throws ExecutionException, InterruptedException {


        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> {

            System.out.println("任务1启动");
            return "abc1";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<String> stringCompletableFuture2 = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务2启动");

            return "abc2";
        }, ThreadPoolProvider.getPool());


        stringCompletableFuture1.runAfterEitherAsync(stringCompletableFuture2, () -> {
            System.out.println("任务3启动");
        }, ThreadPoolProvider.getPool());


    }

    /***
     * 俩个任务，只要有一个完成，就执行下面的任务,感知最先执行完毕任务的结果，自己也没有返回值
     *
     * **/
    public static void test14() throws ExecutionException, InterruptedException {


        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> {

            System.out.println("任务1启动");
            return "abc1";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<String> stringCompletableFuture2 = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务2启动");

            return "abc2";
        }, ThreadPoolProvider.getPool());


        stringCompletableFuture1.acceptEitherAsync(stringCompletableFuture2, x -> {
            System.out.println(x);
        }, ThreadPoolProvider.getPool());


    }


    /***
     * 俩个任务，只要有一个完成，就执行下面的任务,感知最先执行完毕任务的结果，有返回值
     *
     * **/
    public static void test15() throws ExecutionException, InterruptedException {


        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> {

            System.out.println("任务1启动");
            return "abc1";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<String> stringCompletableFuture2 = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务2启动");

            return "abc2";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<String> stringCompletableFuture = stringCompletableFuture1.applyToEitherAsync(stringCompletableFuture2, x -> {
            System.out.println(x);
            return x + "123123";
        }, ThreadPoolProvider.getPool());

        System.out.println(stringCompletableFuture.get());

    }


    /***
     * 多任务组合,等待全部返回
     *
     * **/
    public static void test16() throws ExecutionException, InterruptedException {
        long st = System.currentTimeMillis();
        System.out.println("start:" + st);
        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务1启动");
            return "1";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<String> stringCompletableFuture2 = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务2启动");

            return "2";
        }, ThreadPoolProvider.getPool());

        CompletableFuture<String> stringCompletableFuture3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务3启动");

            return "3";
        }, ThreadPoolProvider.getPool());

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(stringCompletableFuture1, stringCompletableFuture2, stringCompletableFuture3);
        voidCompletableFuture.get();//等待所有结果完成 5181
        stringCompletableFuture1.get();
        stringCompletableFuture2.get();
        stringCompletableFuture3.get();
        long en = System.currentTimeMillis() - st;
        System.out.println("end:" + en);

    }


    /***
     * 多任务组合,有一个返回就执行完毕
     *
     * **/
    public static void test17() throws ExecutionException, InterruptedException {
        long st = System.currentTimeMillis();
        System.out.println("start:" + st);
        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务1启动");
            return "1";
        }, ThreadPoolProvider.getPool());


        CompletableFuture<String> stringCompletableFuture2 = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务2启动");

            return "2";
        }, ThreadPoolProvider.getPool());

        CompletableFuture<String> stringCompletableFuture3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务3启动");

            return "3";
        }, ThreadPoolProvider.getPool());

        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(stringCompletableFuture1, stringCompletableFuture2, stringCompletableFuture3);
        objectCompletableFuture.get();//等待所有结果完成 5181

        long en = System.currentTimeMillis() - st;
        System.out.println("end:" + en);

    }


    /***
     * 任务的链式处理
     *在应用中经常会遇到任务的pipeline处理，任务A执行完后触发任务B，任务B执行完后触发任务C，上一个任务的结果是下一个任务的输入，对于这种场景，我们可以使用thenApply方法。无返回值
     * **/
    public static void test18() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            return "1";
        }).thenApplyAsync(x -> {
            return x + "1";
        }).thenAcceptAsync(b -> {
            System.out.println(b + "1");
        });

    }




    public static void main(String[] args) {

        try {
            //test1();
            // test2();
            test3();
            //test5();
            //test6();
            //test7();
           // test8();
            // test9();
            //test10();
            //test12();
            //test13();
            //test14();
            //test15();
            // test16();
            //test17();
            //test18();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
