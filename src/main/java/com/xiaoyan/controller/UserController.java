package com.xiaoyan.controller;


import com.xiaoyan.context.BaseContext;
import com.xiaoyan.dto.UserDTO;
import com.xiaoyan.pojo.User;
import com.xiaoyan.result.Result;
import com.xiaoyan.service.UserService;
import com.xiaoyan.utils.JwtUtil;
import com.xiaoyan.vo.LoginVO;
import com.xiaoyan.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("users")
@AllArgsConstructor
@Slf4j
@Tag(name = "用户管理")
public class UserController {

    private UserService userService;

    private JwtUtil jwtUtil;

    @PostMapping("register")
    @Operation(summary = "用户注册")
    public Result<LoginVO> register(@RequestBody UserDTO userDTO) throws IOException {
        log.info("用户注册：{}", userDTO);
        User user = new User();
        BeanUtils.copyProperties(userDTO, user, "id", "password");
        userService.register(user);

       String token= jwtUtil.generate(userDTO.getEmail());

        return Result.success(LoginVO.builder().token(token).build());
    }

    @GetMapping
    @Operation(summary = "返回当前用户信息")
    public Result<UserVO> getInfo() {
        UserVO user = userService.getInfo();
        log.info("返回用户信息：{}", user);
        return Result.success(user);
    }

    @PutMapping
    @Operation(summary = "修改用户消息")
    public Result<String> update(@RequestBody UserDTO userDTO) {
        log.info("用户消息修改：{}", userDTO);

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userService.update(user);

        return Result.success();
    }
}
