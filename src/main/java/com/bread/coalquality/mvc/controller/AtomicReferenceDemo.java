package com.bread.coalquality.mvc.controller;

import com.bread.coalquality.mvc.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description: 原子引用类型
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
public class AtomicReferenceDemo {

    public static void main(String[] args) {


        User user1 = User.builder().id(1L).userName("zs").passWord("123").build();
        User user2 = User.builder().id(2L).userName("ls").passWord("321").build();

        AtomicReference<User> userAtomicReference = new AtomicReference<>();
        userAtomicReference.set(user1);
        userAtomicReference.set(user2);
        userAtomicReference.set(user1);


        log.info(userAtomicReference.compareAndSet(user1, user2) +",#####"+userAtomicReference.get().toString() );
        log.info(userAtomicReference.compareAndSet(user1, user2) +",#####"+userAtomicReference.get().toString() );
    }
}


