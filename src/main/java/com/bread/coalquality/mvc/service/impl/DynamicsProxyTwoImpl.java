package com.bread.coalquality.mvc.service.impl;

import cn.hutool.json.JSONUtil;
import com.bread.coalquality.mvc.entity.Product;
import com.bread.coalquality.mvc.service.IDynamicsProxy;

import java.util.Map;

/**
 * @Description: DynamicsProxyOneImpl 代理方法实现one
 * @Author: haoxd
 * @Version: 1.0
 */
public class DynamicsProxyTwoImpl implements IDynamicsProxy {


    @Override
    public void print() {
        System.out.println("我是代理实现类二");
    }

    @Override
    public int sum(int a, int b) {
        return a+b+3;
    }

    @Override
    public String format(Map<String, String> map) {
        return JSONUtil.toJsonStr(map);
    }

    @Override
    public Product getOne(int id) {

        return Product.getData().get(id);

    }
}
