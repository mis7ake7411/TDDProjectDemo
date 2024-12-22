package com.mis7ake7411.tddprojectdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DetailedTestAspect {

  // @Before 測試
  @Before("execution(* com.mis7ake7411.tddprojectdemo.service.DemoService.beforeTest(..))")
  public void beforeAdvice(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    System.out.println("[@Before] 準備執行方法，參數是：" + args[0]);
  }

  // @After 測試
  @After("execution(* com.mis7ake7411.tddprojectdemo.service.DemoService.afterTest(..))")
  public void afterAdvice(JoinPoint joinPoint) {
    System.out.println("[@After] 方法執行完畢，無論是否有異常都會執行");
  }

  // @AfterReturning 測試
  @AfterReturning(
      pointcut = "execution(* com.mis7ake7411.tddprojectdemo.service.DemoService.afterReturningTest(..))",
      returning = "result"
  )
  public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
    System.out.println("[@AfterReturning] 方法正常返回，返回值是：" + result);
  }

  // @AfterThrowing 測試
  @AfterThrowing(
      pointcut = "execution(* com.mis7ake7411.tddprojectdemo.service.DemoService.afterThrowingTest(..))",
      throwing = "error"
  )
  public void afterThrowingAdvice(JoinPoint joinPoint, Throwable error) {
    System.out.println("[@AfterThrowing] 方法拋出異常：" + error.getMessage());
  }

  // @Around 測試
  @Around("execution(* com.mis7ake7411.tddprojectdemo.service.DemoService.aroundTest(..))")
  public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("[@Around] 方法執行前處理");

    long startTime = System.currentTimeMillis();
    Object result = null;

    try {
      result = joinPoint.proceed();
      System.out.println("[@Around] 方法執行完成，返回結果：" + result);
    } catch (Throwable e) {
      System.out.println("[@Around] 方法執行出現異常：" + e.getMessage());
      throw e;
    } finally {
      long endTime = System.currentTimeMillis();
      System.out.println("[@Around] 方法執行耗時：" + (endTime - startTime) + "ms");
    }

    return result;
  }
}
