package com.mis7ake7411.tddprojectdemo.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  // 定義切點：攔截UserService的所有方法
  @Pointcut("within(com.mis7ake7411.tddprojectdemo.service.UserService)")
  public void userServiceMethods() {
  }

  @Pointcut("within(com.mis7ake7411.tddprojectdemo.service.*)")
  public void serviceMethods() {}

  // 前置通知：方法執行前
  @Before("userServiceMethods()")
  public void logBefore(JoinPoint joinPoint) {
    System.out.println("Before method: " +
        joinPoint.getSignature().getName() +
        ", Arguments: " + Arrays.toString(joinPoint.getArgs())
    );
  }

  // 後置通知：方法執行後
  @After("userServiceMethods()")
  public void logAfter(JoinPoint joinPoint) {
    System.out.println("After method: " +
        joinPoint.getSignature().getName() +
        " completed"
    );
  }

  @Around("serviceMethods()")
  public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    try {
      Object proceed = joinPoint.proceed();

      long executionTime = System.currentTimeMillis() - start;
      log.info("Method {} executed in {} ms",
          joinPoint.getSignature(), executionTime);

      return proceed;
    } catch (Exception e) {
      log.error("Exception in {} with message: {}",
          joinPoint.getSignature(), e.getMessage());
      throw e;
    }
  }
}