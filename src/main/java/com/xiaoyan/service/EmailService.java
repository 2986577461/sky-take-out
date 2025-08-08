package com.xiaoyan.service;


import com.xiaoyan.dto.EmailLoginDTO;
import com.xiaoyan.vo.LoginVO;

public interface EmailService{

    void sendVerificationCode(String email);

   LoginVO emailLogin(EmailLoginDTO emailLoginDTO);
}
