package com.mis7ake7411.tddprojectdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortCriteria {
  private String key;         // 排序字段
  private boolean ascending;  // 是否升序
}
