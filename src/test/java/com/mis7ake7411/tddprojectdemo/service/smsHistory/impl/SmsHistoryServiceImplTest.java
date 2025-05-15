package com.mis7ake7411.tddprojectdemo.service.smsHistory.impl;

import com.mis7ake7411.tddprojectdemo.model.bo.QuerySmsHistoryBO;
import com.mis7ake7411.tddprojectdemo.model.dto.PageResponseDTO;
import com.mis7ake7411.tddprojectdemo.model.vo.QuerySmsHistoryVO;
import com.mis7ake7411.tddprojectdemo.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SmsHistoryServiceImplTest {
    @Autowired
    private SmsHistoryServiceImpl smsHistoryService;

    @Test
    void testQuerySmsHistoryData() {
        QuerySmsHistoryBO bo = new QuerySmsHistoryBO();
        bo.setSendDateStart("2025-03-20");
        bo.setSendDateEnd("2025-04-17");
//        bo.setSmsSendType("Y");
        QuerySmsHistoryVO result = smsHistoryService.querySmsHistoryData(bo);
        log.info(JsonUtils.toPrettyJson(result));
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("不帶分頁的查詢")
    void testQuerySmsHistoryDataWithQueryDSL(){
        QuerySmsHistoryBO bo = new QuerySmsHistoryBO();
        bo.setSendDateStart("2025-03-20");
        bo.setSendDateEnd("2025-04-17");

        QuerySmsHistoryVO result = smsHistoryService.querySmsHistoryDataWithQueryDSL(bo);
        log.info(JsonUtils.toPrettyJson(result));
        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("帶有分頁的查詢")
    void testQuerySmsHistoryDataWithQueryDSLPageable(){
        QuerySmsHistoryBO bo = new QuerySmsHistoryBO();
        bo.setSendDateStart("2025-03-20");
        bo.setSendDateEnd("2025-04-17");

        PageResponseDTO<QuerySmsHistoryVO.SmsHistoryDTO> result = smsHistoryService.querySmsHistoryDataWithQueryDSL(bo, 1, 10);
        log.info(JsonUtils.toPrettyJson(result));
        // Assert
        assertNotNull(result);
    }
}