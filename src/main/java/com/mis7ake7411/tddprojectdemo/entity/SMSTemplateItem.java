package com.mis7ake7411.tddprojectdemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;




/**
 * Table Entity : tblMailTemplateitem
 * <br>Email範本項目設定表
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tblSMSTemplateItem")
public class SMSTemplateItem {
    @Id
    @Column(name = "DBID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long DBID;

    @Column(name = "Subject")
    private String Subject;
    @Column(name = "Content")
    private String Content;
    @Column(name = "Status")
    private String Status;
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

}
