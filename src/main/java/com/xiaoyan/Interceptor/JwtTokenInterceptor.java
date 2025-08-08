package com.xiaoyan.Interceptor;


import com.xiaoyan.context.BaseContext;
import com.xiaoyan.properties.JwtProperties;
import com.xiaoyan.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        BaseContext.removeCurrentId();
    }

    @Autowired
    private JwtProperties jwtProperties;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                if (!(handler instanceof HandlerMethod)) {
                        return true;
        }
                String token = request.getHeader(jwtProperties.getTokenName());
                try {
                        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            String id = claims.get(jwtProperties.getTokenName()).toString();
            log.info("当前id：{}", id);
                        BaseContext.setCurrentId(id);
                        return true;
        } catch (Exception ex) {
                        response.setStatus(401);
            return false;
        }
    }
}
