package com.mis7ake7411.tddprojectdemo.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Table Entity : tblCfg_Person
 * <br>人員設定表
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tblCfg_Person")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type", defaultImpl = CfgPerson.class)
public class CfgPerson implements Serializable {

    @Id
    @Column(name = "DBID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long DBID;

    @Column(name = "ACCOUNT")
    private String ACCOUNT;
    @Column(name = "FIRSTNAME")
    private String FIRSTNAME;
    @Column(name = "LASTNAME")
    private String LASTNAME;
    @Column(name = "EMAILADDRESS")
    private String EMAILADDRESS;
    @Column(name = "EMPLOYEE_ID")
    private String EMPLOYEE_ID;
    @Column(name = "USER_NAME")
    private String USER_NAME;
    @Column(name = "STATE")
    private String STATE;
    @Column(name = "DN")
    private String DN;
    @Column(name = "CREATEDATETIME")
    private String CREATEDATETIME;
    @Column(name = "CREATEUSERID")
    private String CREATEUSERID;
    @Column(name = "CH_PASS_ON_LOGIN")
    private String CH_PASS_ON_LOGIN;
    @Column(name = "PASS_ERROR_COUNT")
    private String PASS_ERROR_COUNT;
    @Column(name = "MAX_COUNT")
    private String MAX_COUNT;
    @Column(name = "TenantID")
    private String TenantID;
    @Column(name = "PASSWORD")
    private String PASSWORD;
    @Column(name = "DNPwd")
    private String DNPwd;
    @Column(name = "EnableDNLogin")
    private String EnableDNLogin;
    @Column(name = "AreaCode")
    private String AreaCode;
    @Column(name = "OutboundTelCode")
    private String OutboundTelCode;
    @Column(name = "Language")
    private String Language;
    @Column(name = "CallCenterID")
    private String CallCenterID;
    @Column(name = "ForceResetPassword")
    private String forceResetPassword;
    @Column(name = "picture")
    private String picture;
    @Column(name = "EmailSignature")
    private String emailSignature;
    @Column(name = "CDC_CARD_SN")
    private String cdcCardSN;
    @Column(name = "SubstituteIDs")
    private String SubstituteIDs;
    @Column(name = "SwitchAccount")
    private String switchAccount;
    @Column(name = "SwitchPwd")
    private String switchPwd;
    @Column(name = "PhoneNumber")
    private String phoneNumber;
}