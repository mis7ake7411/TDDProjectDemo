package com.mis7ake7411.tddprojectdemo.enums;

import lombok.Getter;
import java.time.format.DateTimeFormatter;

@Getter
public enum DateTimeFormattersEnum {
  DATE_TIME_WITH_MILLIS_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
  DATE_TIME_WITH_MILLIS_DASH("yyyy-MM-dd HH:mm:ss.SSS"),
  DATE_TIME_WITH_SLASH("yyyy/MM/dd HH:mm:ss"),
  DATE_TIME_WITH_DASH("yyyy-MM-dd HH:mm:ss"),
  DATE_WITH_SLASH("yyyy/MM/dd"),
  DATE_WITH_DASH("yyyy-MM-dd"),
  DATE_YYYYMMDD("yyyyMMdd"),
  TIME_WITH_MILLIS("HH:mm:ss.SSS"),
  TIME_WITH_COLON("HH:mm:ss");

  private final DateTimeFormatter formatter;

  DateTimeFormattersEnum(String pattern) {
    this.formatter = DateTimeFormatter.ofPattern(pattern);
  }

  public static DateTimeFormattersEnum getFormatterEnum(String format) {
    if (format.contains("/")) {
      switch (format) {
        case "yyyy/MM/dd HH:mm:ss.SSS":
          return DATE_TIME_WITH_MILLIS_SLASH;
        case "yyyy/MM/dd":
          return DATE_WITH_SLASH;
        case "yyyy/MM/dd HH:mm:ss":
        default:
          return DATE_TIME_WITH_SLASH;
      }
    } else if (format.contains("-")) {
      switch (format) {
        case "yyyy-MM-dd HH:mm:ss.SSS":
          return DATE_TIME_WITH_MILLIS_DASH;
        case "yyyy-MM-dd":
          return DATE_WITH_DASH;
        case "yyyy-MM-dd HH:mm:ss":
        default:
          return DATE_TIME_WITH_DASH;
      }
    } else {
      switch (format) {
        case "yyyyMMdd":
          return DATE_YYYYMMDD;
        case "HH:mm:ss.SSS":
          return TIME_WITH_MILLIS;
        case "HH:mm:ss":
          return TIME_WITH_COLON;
        default:
          throw new IllegalArgumentException("無效的時間格式 : " + format);
      }
    }
  }
}
