package com.xiaoyan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyan.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE user_name=#{username}")
    User selectByUsername(String username);

    @Select("SELECT * FROM user WHERE email=#{email}")
    User selectByEmail(String email);
}
