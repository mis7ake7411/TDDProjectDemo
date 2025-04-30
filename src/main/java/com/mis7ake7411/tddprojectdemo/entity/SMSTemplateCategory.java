package com.mis7ake7411.tddprojectdemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Table Entity : tblMailTemplateCategory
 * <br>Email範本目錄設定表
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tblSMSTemplateCategory")
public class SMSTemplateCategory {
    @Id
    @Column(name = "DBID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long DBID;

    @Column(name = "ParentID")
    private String ParentID;
    @Column(name = "Name")
    private String Name;
    @Column(name = "Sort")
    private String Sort;
    @Column(name = "DeleteFlag")
    private String DeleteFlag;
    @Column(name = "CreatePersonID")
    private String CreatePersonID;
    @Column(name = "CreateTime")
    private String CreateTime;
    @Column(name = "ModifyPersonID")
    private String ModifyPersonID;
    @Column(name = "ModifyTime")
    private String ModifyTime;
    @Column(name = "TenantID")
    private String TenantID;
    @Column(name = "PilotID")
    private String PilotID;

}
