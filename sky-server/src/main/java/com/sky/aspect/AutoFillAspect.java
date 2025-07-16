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

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-05-20   15:29
 * 自定义 切面类 用于自动填充的切面类处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 指定 切入点 @Pointcut 表示对于哪些类的哪些方法进行拦截
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}
    /**
     * 前置通知 AOP相关概念：切面 切入点 通知
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段的自动填充");
        // 获取当前被拦截的方法上的数据库操作类型
        // 方法签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        // 获得到数据库操作类型
        OperationType operationType = autoFill.value();
        // 获取当前被拦截的方法上的相关参数 实体
        Object[] args = joinPoint.getArgs();

        if (args==null || args.length==0){
            return;
        }
        Object object = args[0];
        // 准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 通过反射赋值
        if(operationType==OperationType.INSERT){
            // 如果是插入操作
            try {
                Method setCreateTimes = object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTimes = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射为对象复制
                setCreateTimes.invoke(object,now);
                setUpdateTimes.invoke(object,now);
                setCreateUser.invoke(object,currentId);
                setUpdateUser.invoke(object,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (operationType==OperationType.UPDATE){
            // 如果是插入操作
            try {

                Method setUpdateTimes = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射为对象复制
                setUpdateTimes.invoke(object,now);
                setUpdateUser.invoke(object,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
