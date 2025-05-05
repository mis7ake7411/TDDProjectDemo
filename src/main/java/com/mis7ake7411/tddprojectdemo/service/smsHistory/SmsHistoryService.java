package com.mis7ake7411.tddprojectdemo.service.smsHistory;

import com.mis7ake7411.tddprojectdemo.model.bo.QuerySmsHistoryBO;
import com.mis7ake7411.tddprojectdemo.model.dto.PageResponseDTO;
import com.mis7ake7411.tddprojectdemo.model.vo.QuerySmsHistoryVO;

public interface SmsHistoryService {
    /**
     * 查詢簡訊歷史資料 (JPA)
     *
     * @param bo 查詢條件
     * @return 簡訊歷史資料
     */
    QuerySmsHistoryVO querySmsHistoryData(QuerySmsHistoryBO bo);

    /**
     * 查詢簡訊歷史資料 (QueryDSL)
     *
     * @param bo 查詢條件
     * @return 簡訊歷史資料
     */
    QuerySmsHistoryVO querySmsHistoryDataWithQueryDSL(QuerySmsHistoryBO bo);

    /**
     * 查詢簡訊歷史資料 (QueryDSL) 分頁
     *
     * @param bo 查詢條件
     * @param pageNumber 頁碼
     * @param pageSize 每頁大小
     * @return 分頁結果
     */
    PageResponseDTO<QuerySmsHistoryVO.SmsHistoryDTO> querySmsHistoryDataWithQueryDSL(QuerySmsHistoryBO bo, int pageNumber, int pageSize);
}
