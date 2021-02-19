package com.bread.coalquality.mvc.controller;

import com.bread.coalquality.mvc.entity.User;

/**
 * @Description: 基本类型传值副本：所以只会在自己方法作用域改变，不会影响其他方法
 * 对象类型传引用：所以会改变
 * 字符串特殊：但是指针指向引用会改变，有则使用，没有创建，所以方法应用是在内部的指向
 * @Author: haoxd
 * @Version: 1.0
 */
public class TransValueDemo {


    private static void change1(int age) {
        age = 30;
    }

    private static void change2(User user) {
        user.setUserName("ABC");
    }

    private static void change3(String str1) {
        str1 = "ABC";
    }

    private static void change4(String str2) {
        str2 = "ABC";
    }


    public static void main(String[] args) {


        int age = 20;
        change1(age);
        System.out.println("age:" + age);



        User user = new User();
        user.setUserName("XXXX");
        change2(user);
        System.out.println("user:" + user.getUserName());



        String str1 = "XXXX";
        change3(str1);
        System.out.println("str1:" + str1);



        String str2 = new String("XXXX");
        change4(str2);
        System.out.println("str2:" + str2);

    }


}
