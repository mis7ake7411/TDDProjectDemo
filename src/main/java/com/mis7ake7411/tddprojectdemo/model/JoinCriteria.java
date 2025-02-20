package com.mis7ake7411.tddprojectdemo.model;

import jakarta.persistence.criteria.JoinType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinCriteria {
  /** 要 Join 的屬性名 */
  private String attribute;
  /** Join 的型態（如：INNER, LEFT） */
  private JoinType joinType;
}
