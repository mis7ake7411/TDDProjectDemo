package com.mis7ake7411.tddprojectdemo.test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LetterCodeTest {

  public static void main(String[] args) {
    List<String> codeList = new ArrayList<>();
    Random random = new Random();

    while(codeList.size() < 4) {
      Set<Character> chars = new LinkedHashSet<>();
      while (chars.size() < 4) {
        // 生成隨機字母和數字
        char c = (char) (random.nextInt(26) + 'A'); // 生成 A-Z 的大寫字母
        chars.add(c);
      }
      // 將 Set 轉換為字串
      String code = chars.stream()
          .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
          .toString();
      // 確保不重複
      if (!codeList.contains(code)) {
        codeList.add(code);
      }
    }

    // 輸出生成的驗證碼
    codeList.forEach(System.out::println);
  }
}
