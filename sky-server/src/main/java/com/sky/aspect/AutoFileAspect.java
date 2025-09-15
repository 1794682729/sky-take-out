package com.sky.aspect;

import com.sky.annotation.AutoFile;
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

@Aspect
@Component
@Slf4j
public class AutoFileAspect {

    /**
     * 指定切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFile)")
    public void autoFilePointcut() {
    }

    /**
     * 前置通知
     */
    @Before("autoFilePointcut()")
    public void before(JoinPoint joinPoint) {
        log.info("before method start");
        //需要获取当前被拦截的方发的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFile autoFile = signature.getMethod().getAnnotation(AutoFile.class);//获取方法上的注解对象
        OperationType operationType = autoFile.value();


        //获取当前被拦截方法的参数,就是当前修改的实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object objects = args[0];

        //准备赋值的数据
        LocalDateTime time = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据当前不同的操作类型来给属性赋值
        try {
            Method setCreatTime = objects.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setCreateUser = objects.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateTime = objects.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = objects.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            if (operationType == OperationType.INSERT) {
                setCreatTime.invoke(objects, time);
                setCreateUser.invoke(objects, currentId);
                setUpdateTime.invoke(objects, time);
                setUpdateUser.invoke(objects, currentId);
            } else if (operationType == OperationType.UPDATE) {
                setUpdateTime.invoke(objects, time);
                setUpdateUser.invoke(objects, currentId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}