package com.xiaoyan.service.impl;

import com.xiaoyan.constant.MessageConstant;
import com.xiaoyan.context.BaseContext;
import com.xiaoyan.dto.EmailLoginDTO;
import com.xiaoyan.exception.ParameterException;
import com.xiaoyan.mapper.UserMapper;
import com.xiaoyan.pojo.User;
import com.xiaoyan.service.EmailService;
import com.xiaoyan.utils.JwtUtil;
import com.xiaoyan.vo.LoginVO;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    public final static Map<String, CodeInfo> codePool = new ConcurrentHashMap<>();

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Data
    @ToString
    public static class CodeInfo {
        private String code;
        private long expireTime;

        public CodeInfo(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
        }
    }

    public void sendVerificationCode(String email) {
                String code = String.format("%06d", (int) (Math.random() * 1000000));

        codePool.put(email, new CodeInfo(code,
                System.currentTimeMillis() + 5 * 60 * 1000));
                SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("马家骑服务平台");
        message.setText("验证码: " + code + "，5分钟内有效");
        mailSender.send(message);
    }

    public LoginVO emailLogin(EmailLoginDTO emailLoginDTO) {
        boolean verified = verifyCode(emailLoginDTO);
        if (!verified)
            throw new ParameterException(MessageConstant.VERIFICATION_CODE_MISMATCH);

        String email = emailLoginDTO.getEmail();
        User user = userMapper.selectByEmail(email);
        if (user != null) {
            String token = jwtUtil.generate(email);
            return LoginVO.builder().token(token).build();
        }
        return null;
    }


    private boolean verifyCode(EmailLoginDTO emailLoginDTO) {
        String email = emailLoginDTO.getEmail();
        String inputCode = emailLoginDTO.getCode();

        CodeInfo codeInfo = codePool.get(email);
        if (codeInfo == null) return false;

        return System.currentTimeMillis() <= codeInfo.getExpireTime()
                && inputCode.equals(codeInfo.getCode());
    }
}