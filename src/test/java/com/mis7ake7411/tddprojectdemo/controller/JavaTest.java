package com.mis7ake7411.tddprojectdemo.controller;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class JavaTest {
  public static void main(String[] args) {
    System.out.println(DateTimeUtil.parseDateStrToLocalDateTime("2024/09/01"));
  }

  public static class DateTimeUtil{
    public static final String DateTime_WITH_SLASH = "yyyy/MM/dd HH:mm:ss";
    public static final String DateTime_WITH_DASH = "yyyy-MM-dd HH:mm:ss";
    public static final String Date_WITH_SLASH = "yyyy/MM/dd";
    public static final String Date_WITH_DASH = "yyyy-MM-dd";
    public static final String Date_YYYYMMDD = "yyyyMMdd";

    public static final  DateTimeFormatter[] formatters = {
          DateTimeFormatter.ofPattern(DateTime_WITH_SLASH),
          DateTimeFormatter.ofPattern(DateTime_WITH_DASH),
          DateTimeFormatter.ofPattern(Date_WITH_SLASH),
          DateTimeFormatter.ofPattern(Date_WITH_DASH),
          DateTimeFormatter.ofPattern(Date_YYYYMMDD)
    };

    public static Object parseDateStrToLocalDateTime(String dateStr) {
      for (DateTimeFormatter formatter : formatters) {
        try {
          if (formatter.toString().contains("HH:mm:ss")) {
            return LocalDateTime.parse(dateStr, formatter);
          } else {
            return LocalDate.parse(dateStr, formatter);
          }
        } catch (DateTimeParseException e) {
          // Continue to the next formatter
        }
      }
      return null;
    }
  }

    // 轉換成base64格式的測試method
  @Test
    public void  testToBase64() {
        String str = "test";
        String base64 = java.util.Base64.getEncoder().encodeToString(str.getBytes());
    System.out.println(base64);
    }



}
