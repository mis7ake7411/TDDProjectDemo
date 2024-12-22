package com.mis7ake7411.tddprojectdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DemoService {

  // 測試 @Before
  public String beforeTest(String input) {
    System.out.println("執行 beforeTest 方法，參數：" + input);
    return "Before 測試結果: " + input;
  }

  // 測試 @After
  public void afterTest(boolean throwError) {
    System.out.println("執行 afterTest 方法");
    if (throwError) {
      throw new RuntimeException("afterTest 拋出的異常");
    }
  }

  // 測試 @AfterReturning
  public String afterReturningTest(String input) {
    System.out.println("執行 afterReturningTest 方法，參數：" + input);
    return "處理完成: " + input;
  }

  // 測試 @AfterThrowing
  public void afterThrowingTest() {
    System.out.println("執行 afterThrowingTest 方法");
    throw new RuntimeException("afterThrowingTest 預期的異常");
  }

  // 測試 @Around
  public String aroundTest(int sleepTime) throws InterruptedException {
    System.out.println("執行 aroundTest 方法，等待時間：" + sleepTime + "ms");
    Thread.sleep(sleepTime);
    return "Around 測試完成";
  }
}
