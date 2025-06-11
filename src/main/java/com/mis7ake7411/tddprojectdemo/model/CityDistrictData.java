package com.mis7ake7411.tddprojectdemo.model;

import lombok.Data;

import java.util.List;

@Data
public class CityDistrictData {
    private String cityName; // 縣市名稱
    private List<DistrictData> districts;
}
