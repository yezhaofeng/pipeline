package com.jlu.common.aop.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import com.jlu.common.aop.annotations.LogExecTime;

/**
 * Created by langshiquan on 18/1/25.
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class LogExecTimeAspect {

    private final Logger logger = LoggerFactory.getLogger("methodExecTimeLog");

    @Pointcut("@annotation(logExecTime)")
    public void logExecAnno(LogExecTime logExecTime) {
    }

    @Around("logExecAnno(logExecTime)")
    public Object logExecAround(ProceedingJoinPoint joinPoint, LogExecTime logExecTime) throws Throwable {
        // 获取方法名字
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());

        Long startTime = System.currentTimeMillis(); // 记录开始时间
        // 执行
        Object object = joinPoint.proceed();
        // 记录结束时间
        Long endTime = System.currentTimeMillis();
        // 打印日志
        logger.info("{}.{} exec time:{}", target.getClass(), currentMethod.getName(), (endTime - startTime));
        return object;
    }
}

