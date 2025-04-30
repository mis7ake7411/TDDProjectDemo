package com.mis7ake7411.tddprojectdemo.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuerySmsHistoryVO {
    private List<SmsHistoryDTO> content;

    @Data
    public static class SmsHistoryDTO {
        private Long smsHistoryID;
        /** 發送日期 */
        private LocalDateTime sendDate;
        /** 接收的手機 */
        private String phoneNumber;
        /** 簡訊項目 */
        private String smsItemName;
        /** 客服人鄖 */
        private String agentName;
        /**  是否為 IVR 發送*/
        private String isIvrSend;
        /** IVR 類別 */
        private String ivrCategory;
    }

}
