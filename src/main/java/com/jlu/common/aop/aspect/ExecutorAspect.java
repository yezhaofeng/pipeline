package com.jlu.common.aop.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * Created by yezhaofeng on 2019/1/25.
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class ExecutorAspect {

    @Pointcut()
    public void executor() {
        // TODO: 2019/2/2
    }
}
