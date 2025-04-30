package com.mis7ake7411.tddprojectdemo.model.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuerySmsHistoryBO {
    /** 發送日期 */
    @NotNull(message = "sendDateStart is required")
    private String sendDateStart;
    /** 發送日期 */
    @NotNull(message = "sendDateEnd is required")
    private String sendDateEnd;
    /** 接收的手機 */
    private String phoneNumber;
    /** 簡訊項目 (範本 ID) */
    private Set<String> smsItemIDList;
    /** 客服人員 */
    private Set<String> agentIDList;
    /** 簡訊發送類型是否為 IVR (Y/N) */
    private String smsSendType = "N";
}
