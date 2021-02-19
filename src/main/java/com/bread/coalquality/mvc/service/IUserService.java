package com.bread.coalquality.mvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.coalquality.mvc.entity.User;

import java.util.List;

public interface IUserService extends IService<User> {



    List<User> selectAll();


    List<User> selectAll2();


    List<User> selectAll3();

}