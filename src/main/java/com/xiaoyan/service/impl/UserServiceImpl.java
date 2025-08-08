package com.xiaoyan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyan.constant.MessageConstant;
import com.xiaoyan.context.BaseContext;
import com.xiaoyan.exception.AccountAlreadyExistsException;
import com.xiaoyan.mapper.UserMapper;
import com.xiaoyan.pojo.User;
import com.xiaoyan.service.UserService;
import com.xiaoyan.vo.UserVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private UserMapper userMapper;

    @Override
    public void register(User user) {
        User user1 = userMapper.selectByEmail(user.getEmail());
        if (user1 != null)
            throw new AccountAlreadyExistsException(MessageConstant.ACCOUNT_ALREADY_EXISTS);

        userMapper.insert(user);
    }

    @Override
    public UserVO getInfo() {
        String email = BaseContext.getCurrentId();
        User user = userMapper.selectByEmail(email);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public void update(User user) {
        if (user.getPassword() != null) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        userMapper.updateById(user);
    }

}
