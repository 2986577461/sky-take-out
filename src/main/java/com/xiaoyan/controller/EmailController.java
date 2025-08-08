package com.xiaoyan.controller;

import com.xiaoyan.dto.EmailLoginDTO;
import com.xiaoyan.dto.SendEmailDTO;
import com.xiaoyan.result.Result;
import com.xiaoyan.service.EmailService;
import com.xiaoyan.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("email")
@AllArgsConstructor
@Slf4j
@Tag(name = "电子邮件")
@Validated
public class EmailController {

    private EmailService emailService;

        @PostMapping("/send")
    @Operation(summary = "发送邮件")
    public Result<String> sendCode(@RequestBody @Valid SendEmailDTO sendEmailDTO) {
        log.info("发送邮件：{}", sendEmailDTO);
        emailService.sendVerificationCode(sendEmailDTO.getEmail());
        return Result.success("验证码已发送");
    }

    @PostMapping("/email-login")
    @Operation(summary = "邮件登录")
    public Result<LoginVO> emailLogin(@RequestBody @Valid EmailLoginDTO emailLoginDTO) {
        log.info("邮件登录:{}", emailLoginDTO);
        return Result.success(emailService.emailLogin(emailLoginDTO));
    }
}