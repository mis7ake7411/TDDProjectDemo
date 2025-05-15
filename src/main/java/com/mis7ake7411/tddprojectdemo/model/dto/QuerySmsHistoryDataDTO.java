package com.mis7ake7411.tddprojectdemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class QuerySmsHistoryDataDTO {
    private Long smsHistoryID;
    /** 發送日期 */
    private LocalDateTime sendDate;
    /** 接收的手機 */
    private String phoneNumber;
    /** 範訊範本分類名稱*/
    private String smsCategoryName;
    /** 簡訊範本名稱*/
    private String smsItemName;
    /** 客服人員*/
    private String agentID;
    /** 是否為 IVR 發送 */
    private String isIvrSend;
    /** IVR 類別 */
    private String ivrCategory;

    private String agentName;
}

