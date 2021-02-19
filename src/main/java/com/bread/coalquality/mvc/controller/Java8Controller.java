package com.bread.coalquality.mvc.controller;

import com.bread.coalquality.mvc.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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





}
