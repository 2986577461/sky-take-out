package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..))" +
            "&& @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut() {
    }

    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
        log.info("公共字段填充");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0)
            return;

        Long id = BaseContext.getCurrentId();
        LocalDateTime now = LocalDateTime.now();

        Object entity = args[0];
        Field updateUser = entity.getClass().getDeclaredField(AutoFillConstant.SET_UPDATE_USER);
        Field updateTime = entity.getClass().getDeclaredField(AutoFillConstant.SET_UPDATE_TIME);


        if (autoFill.value().equals(OperationType.INSERT)) {
            Field createUser = entity.getClass().getDeclaredField(AutoFillConstant.SET_CREATE_USER);
            Field createTime = entity.getClass().getDeclaredField(AutoFillConstant.SET_CREATE_TIME);
            createUser.setAccessible(true);
            createUser.set(entity, id);
            createTime.setAccessible(true);
            createTime.set(entity, now);
        }
        updateUser.setAccessible(true);
        updateUser.set(entity, id);
        updateTime.setAccessible(true);
        updateTime.set(entity, now);


    }
}
