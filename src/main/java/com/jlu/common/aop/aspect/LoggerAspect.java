package com.jlu.common.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/2/23.
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class LoggerAspect {

    // Logger非Spring管理对象，无法通过此方式实现切面
    @Pointcut("execution(* org.slf4j.Logger.*(..))")
    public void log() {

    }

    @Around("log()")
    public Object track(ProceedingJoinPoint point) throws Throwable {
        System.out.println("==============================");
        Object[] args = point.getArgs();
        args[0] = "=======" + args[0];
        Object object = point.proceed(args);
        return object;
    }
}
