package com.bread.coalquality.mvc.controller;

import com.bread.coalquality.mvc.entity.User;
import com.bread.coalquality.mvc.service.IUserService;
import com.bread.coalquality.sys.annotation.BindingError;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:https://gitee.com/jingchu/Java_learn/blob/master/cooper_sp/src/main/java/com/cooper/module/user/controller/UserController.java
 * @Author: haoxd
 * @Version: 1.0
 */
@RequestMapping("api/user")
@RestController
public class UserController {


    @Autowired
    private IUserService userService;

    @BindingError
    @PostMapping("/add")
    public boolean add(@RequestBody @Validated  User user){


        return userService.save(user);
    }


    @GetMapping("/get")
    public Map<String,List<User>> get(){
        Map<String,List<User>> map = Maps.newHashMap();
        map.put("1",userService.selectAll());
        map.put("2",userService.selectAll2());
        map.put("3",userService.selectAll3());

        return map;
    }

    public static void main(String[] args) {


        new HashMap<>();
    }


}
