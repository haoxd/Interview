package com.bread.coalquality.mvc.service;

import com.bread.coalquality.mvc.service.impl.DynamicsProxyOneImpl;
import com.bread.coalquality.mvc.service.impl.DynamicsProxyTwoImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: haoxd
 * @Version: 1.0
 */
public class DynamicsProxyHelper {


    public static IDynamicsProxy getDynamicsProxyInstance(DynamicsProxyEnum key) {

        IDynamicsProxy proxy = StringUtils.equals(key.name(), DynamicsProxyEnum.ONE.name()) ? new DynamicsProxyOneImpl() : new DynamicsProxyTwoImpl();

        DynamicsProxyServiceInvocationHandler dynamicsProxyServiceInvocationHandler = new DynamicsProxyServiceInvocationHandler(proxy);


        return dynamicsProxyServiceInvocationHandler.getProxy(IDynamicsProxy.class);
    }


    public enum DynamicsProxyEnum {

        ONE,

        TWO
    }


    public static void main(String[] args) {
        System.out.println(DynamicsProxyHelper.getDynamicsProxyInstance(DynamicsProxyEnum.TWO).sum(1,2));


    }

}
