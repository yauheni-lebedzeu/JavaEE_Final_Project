package com.gmail.yauheniylebedzeu.service.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j2
public class LoggingAspect {

    @Pointcut("within(com.gmail.yauheniylebedzeu..*)")
    void callAnyMethod(){}

    @Before("callAnyMethod()")
    public void logBeforeCallingAnyMethod(JoinPoint joinPoint) {
        log.info("The method \"" + joinPoint.getSignature() + "\" started working");
    }

    @After("callAnyMethod()")
    public void logAfterCallingAnyMethod(JoinPoint joinPoint) {
        log.info("The method \"" + joinPoint.getSignature() + "\" finished working");
    }
}