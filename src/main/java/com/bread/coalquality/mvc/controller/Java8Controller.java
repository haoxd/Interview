package com.bread.coalquality.mvc.controller;

import com.bread.coalquality.mvc.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
@RequestMapping("/java8")
@RestController
public class Java8Controller {


    private Map<Long, Product> cache = new ConcurrentHashMap<>();

    private Product getProductAndCache(Long id) {
        Product product = null;
        //Key存在，返回Value
        if (cache.containsKey(id)) {
            product = cache.get(id);
        } else {
            //不存在，则获取Value
            //需要遍历数据源查询获得Product
            for (Product p : Product.getData()) {
                if (p.getId().equals(id)) {
                    product = p;
                    break;
                }
            }
            //加入ConcurrentHashMap
            if (product != null)
                cache.put(id, product);
        }
        return product;
    }


    private Product getProductAndCacheCool(Long id) {
        return cache.computeIfAbsent(id, i -> //当Key不存在的时候提供一个Function来代表根据Key获取Value的过程
                Product.getData().stream()
                        .filter(p -> p.getId().equals(i)) //过滤
                        .findFirst() //找第一个，得到Optional<Product>
                        .orElse(null)); //如果找不到Product，则使用null
    }

    /**
     * computeIfAbsent -> 如果 k 存在 则直接返回value 不进行 计算，如果k不存在 则重新计算 返回计算后的value(对 hashMap 中指定 key 的值进行重新计算，如果不存在这个 key，则添加到 hasMap 中)
     * compute ->  如果 k 存在 返回计算后的value，如果key 不存在 也返回计算后的value(对 hashMap 中指定 key 的值进行重新计算)
     * computeIfPresent -> 如果 key 对应的 value 不存在，则返回该 null，如果存在，则返回通过 计算后的值。 如果key 不存在 直接返回null(对 hashMap 中指定 key 的值进行重新计算，前提是该 key 存在于 hashMap 中。)
     **/
    public static void main(String[] args) {
        Map<String, String> cache2 = new HashMap<>();
        cache2.put("1", "1");
        cache2.put("2", "2");
        cache2.put("9", "9");

        String a1 = cache2.computeIfAbsent("1", k -> {

                    System.out.println("k->" + k);
                    return "1";
                }
        );
        String a2 = cache2.computeIfAbsent("3", k -> {

                    System.out.println("k->" + k);
                    return "3";
                }
        );
        System.out.println(a1);
        System.out.println(a2);
        System.out.println("cache2.get(\"3\") :" + cache2.get("3"));

        System.out.println("###########################################################################");

        String a3 = cache2.compute("1", (k, v) -> {

                    System.out.println("k->" + k);
                    System.out.println("v->" + v);
                    return "4";
                }
        );
        System.out.println(a3);

        String a4 = cache2.compute("4", (k, v) -> {

                    System.out.println("k->" + k);
                    System.out.println("v->" + v);
                    return "4";
                }
        );


        System.out.println(a4);

        System.out.println("###########################################################################");

        String s = cache2.computeIfPresent("9", (k, v) -> {

            System.out.println("k->" + k);
            System.out.println("v->" + v);
            return "1000";

        });
        System.out.println(s);
        String s2 = cache2.computeIfPresent("10", (k, v) -> {

            System.out.println("k->" + k);
            System.out.println("v->" + v);
            return "9000";

        });

        System.out.println(s2);
        System.out.println("###########################################################################");


        //方法会先判断指定的 key 是否存在，如果不存在，则添加键值对到 hashMap 中，如果存在，返回计算后的value
        cache2.put("cache2","cache2");
        cache2.merge("cache2", "AVDDD", (oldValue, newValue) -> {

            System.out.println("oldValue->"+oldValue);
            System.out.println("newValue->"+newValue);

           return oldValue + newValue;

        });
        System.out.println(cache2.get("cache2"));
    }


}
