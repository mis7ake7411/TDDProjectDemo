package com.mis7ake7411.tddprojectdemo.util;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Slf4j
public class DateTimeUtils {
    public static final DateTimeFormatter dateTimeFormatterYMDHMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 取得當下時間
     *
     * @return 當下時間字串
     */
    public static String getNowTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(dateTimeFormatterYMDHMS);
    }

    /**
     * 取得當下時間
     * @param format 時間格式
     * @return 當下時間字串
     */
    public static String getNowTime(String format) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 檢查指定的日期時間是否在範圍內
     *
     * @param dateTime       待檢查的日期時間
     * @param startDateTime  範圍的開始日期時間
     * @param endDateTime    範圍的結束日期時間
     * @param format         日期時間格式
     * @return true 如果在範圍內，false 如果不在範圍內或格式無效
     */
    public static boolean isWithinTimeRange(String dateTime, String startDateTime, String endDateTime, String format) {
        log.info("dateTime: {}, startDateTime: {}, endDateTime: {}, format: {}", dateTime, startDateTime, endDateTime, format);
        if (dateTime == null || startDateTime == null || endDateTime == null || format == null) {
            return false;
        }
        // 驗證格式是否有效
        if (!isValidFormat(format)) {
            return false;
        }

        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);

            TemporalAccessor dateTimeObj = parseTemporal(dateTime, dateTimeFormatter);
            TemporalAccessor startDateTimeObj = parseTemporal(startDateTime, dateTimeFormatter);
            TemporalAccessor endDateTimeObj = parseTemporal(endDateTime, dateTimeFormatter);

            return compareTemporal(dateTimeObj, startDateTimeObj) > 0 &&
                    compareTemporal(dateTimeObj, endDateTimeObj) < 0;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 嘗試解析日期時間
     *
     * @param dateTime  日期時間字串
     * @param formatter 日期時間格式
     * @return TemporalAccessor，可能是 LocalDateTime、LocalDate 或 LocalTime
     */
    private static TemporalAccessor parseTemporal(String dateTime, DateTimeFormatter formatter) {
        try {
            return LocalDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(dateTime, formatter);
            } catch (DateTimeParseException ex) {
                return LocalTime.parse(dateTime, formatter);
            }
        }
    }

    /**
     * 比較兩個 TemporalAccessor
     *
     * @param a 第一個 TemporalAccessor
     * @param b 第二個 TemporalAccessor
     * @return 正數表示 a > b，負數表示 a < b，0 表示相等
     */
    private static int compareTemporal(TemporalAccessor a, TemporalAccessor b) {
        if (a instanceof LocalDateTime && b instanceof LocalDateTime) {
            return ((LocalDateTime) a).compareTo((LocalDateTime) b);
        } else if (a instanceof LocalDate && b instanceof LocalDate) {
            LocalDateTime startOfDay = ((LocalDate) a).atStartOfDay();
            LocalDateTime endOfDay = ((LocalDate) b).atStartOfDay();
            return startOfDay.compareTo(endOfDay);
        } else if (a instanceof LocalTime && b instanceof LocalTime) {
            return ((LocalTime) a).compareTo((LocalTime) b);
        } else {
            throw new IllegalArgumentException("Incompatible TemporalAccessor types for comparison");
        }
    }

    /**
     * 檢查格式是否有效
     *
     * @param format 日期時間格式
     * @return true 如果格式有效，false 如果無效
     */
    public static boolean isValidFormat(String format) {
        try {
            DateTimeFormatter a =  DateTimeFormatter.ofPattern(format);
            System.out.println(a);
            return true;
        } catch (IllegalArgumentException e) {
            log.error("無效的時間格式 : {}", format);
            return false;
        }
    }

    /**
     * 支援多格式解析日期時間
     *
     * @param dateTime 日期時間字串
     * @param formats  可接受的日期時間格式列表
     * @return TemporalAccessor，可能是 LocalDateTime、LocalDate 或 LocalTime
     */
    public static TemporalAccessor parseWithDynamicFormats(String dateTime, List<String> formats) {
        for (String format : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                log.info("開始解析時間格式 : {}", format);
                return parseTemporal(dateTime, formatter);
            } catch (DateTimeParseException ignored) {
                // 如果解析失敗，繼續嘗試下一個格式
                log.warn("解析時間格式 : {}  失敗", format);
            }
        }
        throw new DateTimeParseException("找不到有效的時間格式 : " + dateTime, dateTime, 0);
    }
}

