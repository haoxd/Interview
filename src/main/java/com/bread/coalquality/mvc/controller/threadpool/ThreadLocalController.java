package com.bread.coalquality.mvc.controller.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @Description: 使用 ThreadLocal 存放一个 Integer 的值，来暂且代表需要在线程中保存的用户信息，这个值初始是 null。
 * 在业务逻辑中，我先从 ThreadLocal 获取一次值，然后把外部传入的参数设置到 ThreadLocal 中，来模拟从当前上下文获取到用户信息的逻辑，随后再获取一次值，最后输出两次获得的值和线程名称。
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
@RequestMapping("/threadlocal")
@RestController
public class ThreadLocalController {


    private static final ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    private static final ThreadLocal<Boolean> isBroadBandLowConsumerBusinessThreadLocal = ThreadLocal.withInitial(() -> null);


    /**
     * 是否宽带2I抵消业务
     */
    private static boolean isBroadBandLowConsumerBusiness(String lowConsumerSerialNumber, String attrOrderId) {
        String before = Thread.currentThread().getName() + ":" + isBroadBandLowConsumerBusinessThreadLocal.get();
        System.out.println("before:" + before);

        try {

            boolean is = StringUtils.isNotBlank(lowConsumerSerialNumber) && isBroadBandLowConsumerCustOrder(attrOrderId);
            isBroadBandLowConsumerBusinessThreadLocal.set(is);
            String after = Thread.currentThread().getName() + ":" + isBroadBandLowConsumerBusinessThreadLocal.get();
            System.out.println("after:" + after);

            return is;
        } finally {
            isBroadBandLowConsumerBusinessThreadLocal.remove();
        }


    }

    private static boolean isBroadBandLowConsumerCustOrder(String attrOrderId) {

        return StringUtils.equals("1", attrOrderId);
    }


    @GetMapping("wrong")
    public Map wrong(@RequestParam("userId") Integer userId) {
        //设置用户信息之前先查询一次ThreadLocal中的用户信息
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        //设置用户信息到ThreadLocal
        currentUser.set(userId);
        //设置用户信息之后再查询一次ThreadLocal中的用户信息
        String after = Thread.currentThread().getName() + ":" + currentUser.get();
        //汇总输出两次查询结果
        Map result = new HashMap();
        result.put("before", before);
        result.put("after", after);
        return result;
    }


    @GetMapping("right")
    public Map right(@RequestParam("userId") Integer userId) {
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        currentUser.set(userId);
        try {
            String after = Thread.currentThread().getName() + ":" + currentUser.get();
            Map result = new HashMap();
            result.put("before", before);
            result.put("after", after);
            return result;
        } finally {
            //在finally代码块中删除ThreadLocal中的数据，确保数据不串
            currentUser.remove();
        }
    }


    public static void test(Integer userId) {
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        System.out.println("before:" + before);
        currentUser.set(userId);
        try {
            String after = Thread.currentThread().getName() + ":" + currentUser.get();
            System.out.println("after:" + after);


            test222();
        } finally {

            currentUser.remove();
        }
    }

    private static void test222() {
        System.out.println("result ->:" + Thread.currentThread().getName() + "##########->" + currentUser.get());
    }

    public static void test333(String a, String b) {
        String before = Thread.currentThread().getName() + ":" + isBroadBandLowConsumerBusinessThreadLocal.get();
        System.out.println("before:" + before);
        boolean broadBandLowConsumerBusiness = isBroadBandLowConsumerBusiness(a, b);
        String after = Thread.currentThread().getName() + ":" + isBroadBandLowConsumerBusinessThreadLocal.get();
        System.out.println("after:" + after);

        next();
    }

    private static void next() {
        System.out.println("result ->:" + Thread.currentThread().getName() + "##########->" + isBroadBandLowConsumerBusinessThreadLocal.get());


    }


    public static void main(String[] args) {


        /*for (int i = 0; i < 100; i++) {
            final Integer c = i;
            new Thread(() ->
                    //test(c)
                    test333("1", "1")
                    , "T" + i).start();

        }*/
        test333("1", "1");

    }


}
