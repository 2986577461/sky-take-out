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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    public void autoFill(JoinPoint joinPoint) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        log.info("公共字段填充");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0)
            return;

        Long id = BaseContext.getCurrentId();
        LocalDateTime now = LocalDateTime.now();

        Object entity = args[0];
        Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
        Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);


        if (autoFill.value().equals(OperationType.INSERT)) {
            Method setCreateUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
            Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
            setUpdateUser.invoke(entity, id);
            setUpdateTime.invoke(entity, now);
            setCreateUser.invoke(entity, id);
            setCreateTime.invoke(entity, now);
        }
        setUpdateUser.invoke(entity, id);
        setUpdateTime.invoke(entity, now);
    }
}
