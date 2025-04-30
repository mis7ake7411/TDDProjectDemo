package com.mis7ake7411.tddprojectdemo.model.dto;

import lombok.Data;

@Data
public class ExportSmsCountDataDTO {
    /** 範訊範本分類名稱*/
    private String smsCategoryName;
    /** 簡訊範本名稱*/
    private String smsItemName;

    private String isIvrSend;
    /** IVR分類名稱*/
    private String ivrCategory;

    private Long itemCount;

    public ExportSmsCountDataDTO(String smsCategoryName, String isIvrSend, String ivrCategory, String smsItemName, Long itemCount) {
        this.smsCategoryName = smsCategoryName;
        this.isIvrSend = isIvrSend;
        this.ivrCategory = ivrCategory;
        this.smsItemName = smsItemName;
        this.itemCount = itemCount;
    }
}

