package com.xiaoyan.service;

import com.xiaoyan.pojo.User;
import com.xiaoyan.vo.UserVO;

import java.io.IOException;

public interface UserService {

    void register( User user) throws IOException;

    UserVO getInfo();

    void update(User user);

}
