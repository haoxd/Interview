package com.bread.coalquality.mvc.service;

import com.bread.coalquality.mvc.entity.Product;

import java.util.Map;

/**
 * @Description:
 * @Author: haoxd
 * @Version: 1.0
 */
public interface IDynamicsProxy {


    void print();

    int sum(int a,int b);

    String format(Map<String,String> map);

    Product getOne(int id);
}
