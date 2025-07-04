package com.sky.mapper;


import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {


    @Select("SELECT * FROM user WHERE openid=#{openId}")
    User getByOpenId(String openId);

    Long insert(User user);

    @Select("SELECT * FROM user WHERE id=#{userId}")
    User getById(Long userId);
}
