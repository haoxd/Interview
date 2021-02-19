package com.bread.coalquality.mvc.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description:
 * @Author: haoxd
 * @Version: 1.0
 */
public class DynamicsProxyServiceInvocationHandler implements InvocationHandler {

    /**
     * 目标对象
     */
    private IDynamicsProxy target;


    /**
     * 构造函数
     *
     * @param target
     */
    public DynamicsProxyServiceInvocationHandler(IDynamicsProxy target) {
        super();
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object result = method.invoke(target, args);
        return result;
    }


    /**
     * 创建代理实例
     *
     * @return
     * @throws Throwable
     */
    public <T> T  getProxy(Class<T> clz){
        return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),this.target.getClass().getInterfaces(), this);

    }
}
