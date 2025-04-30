package com.mis7ake7411.tddprojectdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SportType {
  private Long id;
  private String cnName;
  private String enName;
}
