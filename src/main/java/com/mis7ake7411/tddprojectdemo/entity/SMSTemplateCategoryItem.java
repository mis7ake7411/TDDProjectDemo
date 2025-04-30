package com.mis7ake7411.tddprojectdemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Table Entity : tblSMSTemplateCategory_item
 * <br>簡訊範本目錄項目關聯表
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tblSMSTemplateCategory_Item")
public class SMSTemplateCategoryItem {
    @Id
    @Column(name = "DBID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long DBID;

    @Column(name = "CategoryID")
    private String CategoryID;
    @Column(name = "itemID")
    private String itemID;
    @Column(name = "Sort")
    private String Sort;

}
