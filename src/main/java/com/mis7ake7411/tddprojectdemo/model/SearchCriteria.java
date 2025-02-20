package com.mis7ake7411.tddprojectdemo.model;

import com.mis7ake7411.tddprojectdemo.enums.SearchOperationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCriteria<T> {
    private String key; // 查詢table的欄位
    private T value; // 查詢值
    private SearchOperationEnum operation; // 查詢操作（等於、like、大於、小於等）
}
