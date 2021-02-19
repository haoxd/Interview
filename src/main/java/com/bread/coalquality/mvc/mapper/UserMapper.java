package com.bread.coalquality.mvc.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bread.coalquality.mvc.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    //注解式
    @Select("select * from tb_user ${ew.customSqlSegment}")
    List<User> selectAll(@Param(Constants.WRAPPER)Wrapper<User> wrapper);

    //xml文件式，myabtis-plus方式
    List<User> selectAll2(@Param(Constants.WRAPPER)Wrapper<User> wrapper);

    //xml文件式，myabtis原生方式
    List<User> selectAll3(User user);
}