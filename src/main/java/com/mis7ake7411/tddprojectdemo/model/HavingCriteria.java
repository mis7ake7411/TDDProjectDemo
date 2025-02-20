package com.mis7ake7411.tddprojectdemo.model;

import com.mis7ake7411.tddprojectdemo.enums.SearchOperationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HavingCriteria<T> {
  /** 欄位名稱 */
  private String key;
  /** 聚合函數（SUM、COUNT、AVG 等）*/
  private String aggregationFunction;
  /** 查詢操作（等於、like、大於、小於等） */
  private SearchOperationEnum operation;
  /** 查詢值 */
  private T value;
}
