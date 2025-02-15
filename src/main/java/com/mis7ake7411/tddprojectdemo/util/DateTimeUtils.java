package com.mis7ake7411.tddprojectdemo.util;

import com.mis7ake7411.tddprojectdemo.enums.DateTimeFormattersEnum;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;

@Slf4j
public class DateTimeUtils {
    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormattersEnum.DATE_TIME_WITH_DASH.getFormatter();

    /**
     * 取得當下時間
     *
     * @return 當下時間字串
     */
    public static String getNowTime() {
        LocalDateTime now = LocalDateTime.now();
        return localDateTimeToString(now, null);
    }

    /**
     * 取得當下時間
     *
     * @param format 時間格式
     * @return 當下時間字串
     */
    public static String getNowTime(String format) {
        LocalDateTime now = LocalDateTime.now();
        return localDateTimeToString(now, format);
    }

    /**
     * 將 LocalDateTime 轉換為字串
     *
     * @param localDateTime LocalDateTime
     * @param format 時間格式
     * @return 時間字串
     */
    public static String localDateTimeToString(LocalDateTime localDateTime, String format) {
        if(localDateTime == null || getValidFormat(format) == null) {
            return null;
        }
        if(format != null) {
            return localDateTime.format(DateTimeFormattersEnum.getFormatterEnum(format).getFormatter());
        }
        return localDateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * 將字串轉換為時間物件
     *
     * @param dateTimeStr 時間字串
     * @param format 時間格式
     * @return TemporalAccessor 物件 (LocalDateTime 或 LocalDate)
     * @throws DateTimeParseException 如果解析失敗
     */
    public static TemporalAccessor stringToTemporal(String dateTimeStr, String format) {
        if (dateTimeStr == null || getValidFormat(format) == null) {
            return null;
        }

        DateTimeFormatter formatter = format != null 
            ? DateTimeFormattersEnum.getFormatterEnum(format).getFormatter() 
            : DEFAULT_FORMATTER;

        // 檢查格式是否包含時間元素
        boolean hasTimeElement = format != null && 
            (format.contains("H") || format.contains("h") || 
             format.contains("m") || format.contains("s"));

        try {
            if (hasTimeElement) {
                return LocalDateTime.parse(dateTimeStr, formatter);
            } else {
                return LocalDate.parse(dateTimeStr, formatter);
            }
        } catch (DateTimeParseException e) {
            log.error("解析時間字串失敗: {}, 格式: {}", dateTimeStr, format, e);
            throw e;
        }
    }

    /**
     * 將字串轉換為 LocalDateTime
     *
     * @param dateTimeStr 時間字串
     * @param format 時間格式
     * @return LocalDateTime 物件
     * @throws IllegalArgumentException 如果格式不包含時間元素
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeStr, String format) {
        TemporalAccessor temporal = stringToTemporal(dateTimeStr, format);
        if (temporal instanceof LocalDateTime) {
            return (LocalDateTime) temporal;
        }
        throw new IllegalArgumentException("無法將字串轉換為 LocalDateTime，格式不包含時間元素");
    }

    /**
     * 將字串轉換為 LocalDate
     *
     * @param dateStr 日期字串
     * @param format 日期格式
     * @return LocalDate 物件
     * @throws IllegalArgumentException 如果格式包含時間元素
     */
    public static LocalDate stringToLocalDate(String dateStr, String format) {
        TemporalAccessor temporal = stringToTemporal(dateStr, format);
        if (temporal instanceof LocalDate) {
            return (LocalDate) temporal;
        } else if (temporal instanceof LocalDateTime) {
            return ((LocalDateTime) temporal).toLocalDate();
        }
        throw new IllegalArgumentException("無法將字串轉換為 LocalDate");
    }


    /**
     * 將 LocalDateTime 轉換為 Date
     *
     * @param localDateTime LocalDateTime 物件
     * @return Date 物件
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 將 Date 轉換為 LocalDateTime
     *
     * @param date Date 物件
     * @return LocalDateTime 物件
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
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

        try {
            DateTimeFormatter dateTimeFormatter = getValidFormat(format);

            TemporalAccessor dateTimeObj = parseTemporal(dateTime, dateTimeFormatter);
            TemporalAccessor startDateTimeObj = parseTemporal(startDateTime, dateTimeFormatter);
            TemporalAccessor endDateTimeObj = parseTemporal(endDateTime, dateTimeFormatter);

            return compareTemporal(dateTimeObj, startDateTimeObj) >= 0 &&
                    compareTemporal(dateTimeObj, endDateTimeObj) <= 0;
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
     * @param startDate 第一個 TemporalAccessor
     * @param endDate 第二個 TemporalAccessor
     * @return 正數表示 startDate > endDate，負數表示 startDate < endDate，0 表示相等
     */
    private static int compareTemporal(TemporalAccessor startDate, TemporalAccessor endDate) {
        if (startDate instanceof LocalDateTime && endDate instanceof LocalDateTime) {
            return ((LocalDateTime) startDate).compareTo((LocalDateTime) endDate);
        } else if (startDate instanceof LocalDate && endDate instanceof LocalDate) {
            LocalDateTime startOfDay = ((LocalDate) startDate).atStartOfDay();
            LocalDateTime endOfDay = ((LocalDate) endDate).atStartOfDay();
            return startOfDay.compareTo(endOfDay);
        } else if (startDate instanceof LocalTime && endDate instanceof LocalTime) {
            return ((LocalTime) startDate).compareTo((LocalTime) endDate);
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
        if(format == null) {
            return false;
        }
        try {
          DateTimeFormattersEnum.getFormatterEnum(format);
          return true;
        } catch (IllegalArgumentException e) {
            try {
              DateTimeFormatter.ofPattern(format);
              return true;
            } catch (IllegalArgumentException ex) {
                if (log.isErrorEnabled()) {
                    log.error("無效的時間格式 : {}", format);
                }
                return false;
            }
        }
    }

    /**
     * 取得有效的日期時間格式
     *
     * @param format 日期時間格式
     * @return DateTimeFormatter
     */
    public static DateTimeFormatter getValidFormat(String format) {
        if(format == null) {
            return null;
        }
        try {
            return DateTimeFormattersEnum.getFormatterEnum(format).getFormatter();
        } catch (IllegalArgumentException e) {
            try {
                return DateTimeFormatter.ofPattern(format);
            } catch (IllegalArgumentException ex) {
                if (log.isErrorEnabled()) {
                    log.error("無效的時間格式 : {}", format);
                }
                return null;
            }
        }
    }

    /**
     * 動態解析日期時間格式
     *
     * @param dateTime 日期時間字串
     * @param formats  可接受的日期時間格式列表
     * @return TemporalAccessor，可能是 LocalDateTime、LocalDate 或 LocalTime
     * @throws DateTimeParseException 如果所有格式都無法解析
     */
    public static TemporalAccessor parseWithDynamicFormats(String dateTime, List<String> formats) {
        for (String format : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormattersEnum.getFormatterEnum(format).getFormatter();
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

