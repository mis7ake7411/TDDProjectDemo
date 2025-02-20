package com.mis7ake7411.tddprojectdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortCriteria {
  /** 排序欄位名稱 */
  private String key;
  /** 是否升序 */
  private boolean ascending;
}
