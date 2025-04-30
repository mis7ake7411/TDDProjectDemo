package com.mis7ake7411.tddprojectdemo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tblSMSHistory")
@Data
public class SmsHistory {
    @Id
    @Column(name = "DBID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dbid;

    @Column(name = "IxnID", nullable = false, length = 50)
    private String ixnID;

    @Column(name = "PhoneNumber", nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "SMSContent", nullable = false, length = 255)
    private String smsContent;

    @Column(name = "SendDateTime", nullable = false)
    private LocalDateTime sendDateTime;

    @Column(name = "AgentID",nullable = false, length = 50)
    private String agentID;
    /** 簡訊範本 ID ( tblSMSTemplateItem )*/
    @Column(name = "SmsItemID", nullable = false, length = 50)
    private String smsItemID;
    /** 是否為 IVR 發送( Y/N )，預設 N */
    @Column(name ="IsIVRSend", columnDefinition = "VARCHAR(10) DEFAULT 'N'")
    private String isIvrSend;
    /** IVR 類別 */
    @Column(name = "IvrCategory", nullable = false, length = 100)
    private String ivrCategory;
}

