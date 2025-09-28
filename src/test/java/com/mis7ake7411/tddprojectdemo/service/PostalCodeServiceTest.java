package com.mis7ake7411.tddprojectdemo.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mis7ake7411.tddprojectdemo.model.CityDistrictData;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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