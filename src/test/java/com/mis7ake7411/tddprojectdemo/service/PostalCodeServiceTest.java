package com.mis7ake7411.tddprojectdemo.service;

import com.mis7ake7411.tddprojectdemo.model.CityDistrictData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostalCodeServiceTest {
    @Autowired
    private PostalCodeService postalCodeService;

    @Test
    void testLoadPostalCodes() {
        List<CityDistrictData> data = postalCodeService.getAllCityDistrictData();
        assertNotNull(data);
        assertFalse(data.isEmpty());

        CityDistrictData taipei = data.stream()
                .filter(c -> c.getCityName().equals("臺北市"))
                .findFirst()
                .orElse(null);
        assertNotNull(taipei);
        assertTrue(taipei.getDistricts().stream().anyMatch(d -> d.getDistrictName().equals("中正區")));
    }
}