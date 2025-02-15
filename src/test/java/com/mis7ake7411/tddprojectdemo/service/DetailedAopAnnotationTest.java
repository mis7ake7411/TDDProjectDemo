package com.mis7ake7411.tddprojectdemo.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class DetailedAopAnnotationTest {

  @Autowired
  private DemoService demoService;

  // 預期輸出順序：
  // 1. [@Before] 準備執行方法...
  // 2. 執行 beforeTest 方法...
  @Test
  @DisplayName("測試 @Before 注解")
  public void testBeforeAnnotation() {
    log.info("\n=== @Before 注解測試 ===");
    String result = demoService.beforeTest("測試數據");
    assertNotNull(result);
    System.out.println("-> " + result);
  }

  // 預期輸出順序：
  // 1. 執行 afterTest 方法
  // 2. [@After] 方法執行完畢...
  @Test
  @DisplayName("測試 @After 注解 - 正常執行")
  public void testAfterAnnotationNormal() {
    log.info("\n=== @After 注解測試 (正常執行) ===");
    demoService.afterTest(false);
  }

  // 預期輸出順序：
  // 1. 執行 afterTest 方法
  // 2. [@After] 方法執行完畢...（即使有異常也會執行）
  @Test
  @DisplayName("測試 @After 注解 - 異常執行")
  public void testAfterAnnotationWithException() {
    log.info("\n=== @After 注解測試 (異常執行) ===");
    assertThrows(RuntimeException.class, () -> {
      demoService.afterTest(true);
    });
  }

  // 預期輸出順序：
  // 1. 執行 afterReturningTest 方法...
  // 2. [@AfterReturning] 方法正常返回...
  @Test
  @DisplayName("測試 @AfterReturning 注解")
  public void testAfterReturningAnnotation() {
    log.info("\n=== @AfterReturning 注解測試 ===");
    String result = demoService.afterReturningTest("返回測試");
    System.out.println("-> " + result);
    assertNotNull(result);
  }

  // 預期輸出順序：
  // 1. 執行 afterThrowingTest 方法
  // 2. [@AfterThrowing] 方法拋出異常...
  @Test
  @DisplayName("測試 @AfterThrowing 注解")
  public void testAfterThrowingAnnotation() {
    log.info("\n=== @AfterThrowing 注解測試 ===");
    assertThrows(RuntimeException.class, () -> {
      demoService.afterThrowingTest();
    });
  }

  // 預期輸出順序：
  // 1. [@Around] 方法執行前處理
  // 2. 執行 aroundTest 方法...
  // 3. [@Around] 方法執行完成...
  // 4. [@Around] 方法執行耗時...
  @Test
  @DisplayName("測試 @Around 注解")
  public void testAroundAnnotation() throws InterruptedException {
    log.info("\n=== @Around 注解測試 ===");
    String result = demoService.aroundTest(5000); // 執行1秒
    assertNotNull(result);
  }
}
