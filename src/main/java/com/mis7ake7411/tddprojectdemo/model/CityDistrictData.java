package com.mis7ake7411.tddprojectdemo.model;

import java.util.List;
import lombok.Data;

@Data
public class CityDistrictData {
    private String cityName; // 縣市名稱
    private List<DistrictData> districts;
}
