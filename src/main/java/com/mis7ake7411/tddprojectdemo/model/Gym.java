package com.mis7ake7411.tddprojectdemo.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gym {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private String cnName;
  private String enName;
  private String cnAddress;
  private String enAddress;
  private String startTime;
  private String endTime;
  private String tel;
  private Long districtId;
  private Long sportTypeId;
}
