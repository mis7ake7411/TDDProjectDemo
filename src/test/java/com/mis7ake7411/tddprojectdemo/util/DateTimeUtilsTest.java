package com.mis7ake7411.tddprojectdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DateTimeUtilsTest {

    @Test
    void testIsWithinTimeRange() {
        String format = "yyyy-MM-dd HH:mm:ss";
        boolean result = DateTimeUtils.isWithinTimeRange(
                "2025-02-11 12:00:00",
                "2025-02-11 09:00:00",
                "2025-02-11 21:00:00",
                format
        );
        log.info("在時間範圍內: {}, 格式 : {}", result, format);

         result = DateTimeUtils.isWithinTimeRange(
                DateTimeUtils.getNowTime(),
                "2025-02-11 09:00:00",
                "2025-02-11 21:00:00",
                format
        );
        log.info("在時間範圍內: {}, 格式 : {}", result, format);

        format = "yyyy-MM-dd";
        result = DateTimeUtils.isWithinTimeRange(
                DateTimeUtils.getNowTime(format),
                "2025-02-11",
                "2025-02-11",
                format
        );
        log.info("在時間範圍內: {}, 格式 : {}", result, format);

        format = "HH:mm";
        result = DateTimeUtils.isWithinTimeRange(
                DateTimeUtils.getNowTime(format),
                "09:00",
                "21:00",
                format
        );
        log.info("在時間範圍內: {}, 格式 : {}", result, format);
    }

    @Test
    void testIsValidFormat() {
        boolean valid = DateTimeUtils.isValidFormat("XXX");
        assertTrue(valid);
    }

    @Test
    void testParseWithDynamicFormats() {
        List<String> formats = Arrays.asList("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss");
        TemporalAccessor parsed = DateTimeUtils.parseWithDynamicFormats("17:05:05", formats);
        log.info("testParseWithDynamicFormats : {}",parsed); // LocalTime: 17:05:05
    }

}