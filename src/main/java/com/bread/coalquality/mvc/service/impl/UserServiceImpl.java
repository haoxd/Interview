package com.bread.coalquality.mvc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.coalquality.mvc.entity.User;
import com.bread.coalquality.mvc.mapper.UserMapper;
import com.bread.coalquality.mvc.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {



    @Override
    public List<User> selectAll() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.like(User::getUserName, "张");

       return  baseMapper.selectAll(lambdaQuery);
    }

    @Override
    public List<User> selectAll2() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.like(User::getUserName, "张");
        return  baseMapper.selectAll2(lambdaQuery);
    }

    @Override
    public List<User> selectAll3() {
        return baseMapper.selectAll3(User.builder().id(1267798387853107201L).build());
    }
}