package com.mis7ake7411.tddprojectdemo.repository;

import com.mis7ake7411.tddprojectdemo.model.bo.QuerySmsHistoryBO;
import com.mis7ake7411.tddprojectdemo.model.dto.QuerySmsHistoryDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SmsHistoryQueryRepository {

    Page<QuerySmsHistoryDataDTO> querySmsHistoryDataWithQueryDSL(
            QuerySmsHistoryBO querySmsHistoryBO,
            Pageable pageable
    );
}
