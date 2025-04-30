package com.mis7ake7411.tddprojectdemo.service.smsHistory.impl;

import com.mis7ake7411.tddprojectdemo.model.bo.QuerySmsHistoryBO;
import com.mis7ake7411.tddprojectdemo.model.vo.QuerySmsHistoryVO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SmsHistoryServiceImplTest {
    @Autowired
    private SmsHistoryServiceImpl smsHistoryService;

    @Test
    void testQuerySmsHistoryData() {
        // Arrange
        QuerySmsHistoryBO bo = new QuerySmsHistoryBO();
        bo.setSendDateStart("2025-01-01");
        bo.setSendDateEnd("2026-01-31");
        // Act
        QuerySmsHistoryVO result = smsHistoryService.querySmsHistoryData(bo);
        // Assert
        assertNotNull(result);
    }
}