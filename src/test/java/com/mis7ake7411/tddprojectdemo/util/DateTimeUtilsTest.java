package com.mis7ake7411.tddprojectdemo.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mis7ake7411.tddprojectdemo.enums.DateTimeFormattersEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DateTimeUtilsTest {
    private final LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
    private final List<String> formats = Arrays.asList("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss", "yyyyMMdd", "/", "-", "xxx");

    @Test
    @DisplayName("測試取得當下時間轉字串")
    void testGetCurrentDateTimeToString() {
        String currentDateTime = DateTimeUtils.getNowTime();
        log.info("testGetCurrentDateTime : {}", currentDateTime);
        assertEquals(localDateTime, DateTimeUtils.stringToLocalDateTime(currentDateTime, DateTimeFormattersEnum.DATE_TIME_WITH_DASH.getPattern()));
        String currentDateTime2 = DateTimeUtils.getNowTime(DateTimeFormattersEnum.DATE_TIME_WITH_SLASH.getPattern());
        log.info("testGetCurrentDateTime : {}", currentDateTime2);
        assertEquals(localDateTime, DateTimeUtils.stringToLocalDateTime(currentDateTime2, DateTimeFormattersEnum.DATE_TIME_WITH_SLASH.getPattern()));
    }

    @Test
    @DisplayName("測試取得當下時間是否在時間範圍內")
    void testIsWithinTimeRange() {
        formats.forEach(format -> {
            boolean result = DateTimeUtils.isWithinTimeRange(
                    "2025-02-11 12:00:00",
                    "2025-02-11 09:00:00",
                    "2025-02-11 21:00:00",
                    format
            );
            log.info("在時間範圍內: {}, 格式 : {} \n", result, format);
            result = DateTimeUtils.isWithinTimeRange(
                    "2025-02-11",
                    "2025-02-11",
                    "2025-02-11",
                    format
            );
            log.info("在時間範圍內: {}, 格式 : {} \n", result, format);
            result = DateTimeUtils.isWithinTimeRange(
                    "12:00:00",
                    "09:00:00",
                    "21:00:00",
                    format
            );
            log.info("在時間範圍內: {}, 格式 : {} \n", result, format);
            result = DateTimeUtils.isWithinTimeRange(
                    "20250211",
                    "20250211",
                    "20250211",
                    format
            );
            log.info("在時間範圍內: {}, 格式 : {} \n", result, format);
        });
    }

    @Test
    @DisplayName("測試取得當下時間是否符合格式")
    void testIsValidFormat() {
        AtomicBoolean valid = new AtomicBoolean(false);
        formats.forEach(format -> {
            valid.set(DateTimeUtils.isValidFormat(format));
            assertTrue(valid.get());
            log.info("測試格式是否有效 : {}, 格式 : {} ", valid.get(), format);
        });
    }

    @Test
    @DisplayName("動態解析日期時間格式")
    void testParseWithDynamicFormats() {
        TemporalAccessor parsed = DateTimeUtils.parseWithDynamicFormats("2025-02-15 17:05:05.999", formats);
        log.info("testParseWithDynamicFormats : {}", parsed); // LocalTime: 17:05:05
    }

    @Test
    @DisplayName("測試 LocalDateTime 轉字串")
    void testLocalDateTimeToString() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 3, 20, 15, 30, 0);

        // 測試指定格式
        String result = DateTimeUtils.localDateTimeToString(dateTime, DateTimeFormattersEnum.DATE_TIME_WITH_DASH.getPattern());
        assertEquals("2024-03-20 15:30:00", result);
        String result2 = DateTimeUtils.localDateTimeToString(dateTime, DateTimeFormattersEnum.DATE_TIME_WITH_SLASH.getPattern());
        assertEquals("2024/03/20 15:30:00", result2);

        // 測試 null 值
        assertNull(DateTimeUtils.localDateTimeToString(null, "yyyy-MM-dd HH:mm:ss"));

        // 測試無效格式
        assertNull(DateTimeUtils.localDateTimeToString(dateTime, "invalid format"));
    }

    @Test
    @DisplayName("測試字串轉 TemporalAccessor")
    void testStringToTemporal() {
        TemporalAccessor parsed = DateTimeUtils.stringToTemporal("2025-02-15 17:05:05.999", DateTimeFormattersEnum.DATE_TIME_WITH_MILLIS_DASH.getPattern());
        TemporalAccessor parsed2 = DateTimeUtils.stringToTemporal("2025/02/15", DateTimeFormattersEnum.DATE_WITH_SLASH.getPattern());

        log.info("testStringToTemporal : {}", parsed); // LocalDateTime: 2025-02-15T17:05:05.999
        assertEquals(LocalDateTime.class, parsed.getClass());

        log.info("testStringToTemporal : {}", parsed2);
        assertEquals(LocalDate.class, parsed2.getClass());
    }

    @Test
    @DisplayName("測試字串轉 LocalDateTime")
    void testStringToLocalDateTime() {
        String dateTimeStr = "2024-03-20 15:30:00";
        LocalDateTime result = DateTimeUtils.stringToLocalDateTime(dateTimeStr, DateTimeFormattersEnum.DATE_TIME_WITH_DASH.getPattern());

        assertEquals(
            LocalDateTime.of(2024, 3, 20, 15, 30, 0),
            result
        );

        // 測試純日期格式，應該拋出異常
        assertThrows(IllegalArgumentException.class, () ->
            DateTimeUtils.stringToLocalDateTime("2024-03-20", "yyyy-MM-dd")
        );
    }

    @Test
    @DisplayName("測試字串轉 LocalDate")
    void testStringToLocalDate() {
        // 測試純日期字串
        String dateStr = "2024-03-20";
        LocalDate result = DateTimeUtils.stringToLocalDate(dateStr, DateTimeFormattersEnum.DATE_WITH_DASH.getPattern());
        assertEquals(LocalDate.of(2024, 3, 20), result);

        // 測試日期時間字串，應該只取日期部分
        String dateTimeStr = "2024-03-20 15:30:00";
        LocalDateTime dateTimeResult = DateTimeUtils.stringToLocalDateTime(dateTimeStr, DateTimeFormattersEnum.DATE_TIME_WITH_DASH.getPattern());
        assertEquals(LocalDateTime.of(2024, 3, 20,15,30,0), dateTimeResult);
    }

    @Test
    @DisplayName("測試格式驗證")
    void testFormatValidation() {
        // 測試有效格式
        assertTrue(DateTimeUtils.isValidFormat("yyyy-MM-dd HH:mm:ss"));
        assertTrue(DateTimeUtils.isValidFormat("yyyy-MM-dd"));

        // 測試無效格式
        assertFalse(DateTimeUtils.isValidFormat("invalid format"));
        assertFalse(DateTimeUtils.isValidFormat(null));
    }

    @Test
    @DisplayName("測試 LocalDateTime 和 Date 的互相轉換")
    void testDateTimeConversion() {
        // 測試 LocalDateTime 轉 Date
        LocalDateTime dateTime = LocalDateTime.of(2024, 3, 20, 15, 30, 0);
        Date date = DateTimeUtils.localDateTimeToDate(dateTime);
        log.info("testDateTimeConversion : {}", date);
        assertEquals(Date.class, date.getClass());
        assertNotNull(date);

        // 測試 Date 轉回 LocalDateTime
        LocalDateTime convertedDateTime = DateTimeUtils.dateToLocalDateTime(date);
        log.info("testDateTimeConversion : {}", convertedDateTime);
        assertEquals(LocalDateTime.class, convertedDateTime.getClass());
        assertEquals(dateTime.getYear(), convertedDateTime.getYear());
        assertEquals(dateTime.getMonth(), convertedDateTime.getMonth());
        assertEquals(dateTime.getDayOfMonth(), convertedDateTime.getDayOfMonth());
        assertEquals(dateTime.getHour(), convertedDateTime.getHour());
        assertEquals(dateTime.getMinute(), convertedDateTime.getMinute());

        // 測試 null 值
        assertNull(DateTimeUtils.localDateTimeToDate(null));
        assertNull(DateTimeUtils.dateToLocalDateTime(null));
    }

    @Test
    @DisplayName("測試取得 n 天後的日期")
    void testGetFutureTimer() {
        String dateTime = "2024-03-20 15:30:00";
        int n = 3;
        String  futureTime = DateTimeUtils.getAdjustedDateTime(dateTime, n, DateTimeFormattersEnum.DATE_TIME_WITH_DASH.getPattern());
        log.info("取得 {} 天後的日期 : {}", n, futureTime);
        assertEquals("2024-03-23 15:30:00" , futureTime);
        String date = "2024/03/20";
        String futureDate = DateTimeUtils.getAdjustedDateTime(date, n, DateTimeFormattersEnum.DATE_WITH_SLASH.getPattern());
        log.info("取得 {} 天後的日期 : {}", n, futureDate);
        assertEquals("2024/03/23" , futureDate);
    }

    @Test
    @DisplayName("測試 LocalTime 轉字串")
    void testLocalTimeToString(){
        LocalTime localTime = LocalTime.of(15, 30, 0);
        String timestamp = DateTimeUtils.localTimeToString(localTime, DateTimeFormattersEnum.TIME_WITH_COLON.getPattern());
        log.info("testLocalTimeToString : {}", timestamp);
        assertEquals("15:30:00", timestamp);
    }

    @Test
    @DisplayName("測試相差幾天")
    void testGetDaysBetween() {
        String start = "2024-03-20";
        String end = "2024-03-23";
        long days = DateTimeUtils.getDaysBetween(start, end, DateTimeFormattersEnum.DATE_WITH_DASH.getPattern());
        log.info("testGetDaysBetween : {}", days);
        assertEquals(3, days);
    }
}