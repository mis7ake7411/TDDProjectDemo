package com.mis7ake7411.tddprojectdemo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mis7ake7411.tddprojectdemo.model.CityDistrictData;
import com.mis7ake7411.tddprojectdemo.util.AddressValidator;
import com.mis7ake7411.tddprojectdemo.util.JsonUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class PostalCodeService {
    @Value("classpath:json/taiwanDistricts_zh-TW.json")
    private Resource postalCodeResource;

    private List<CityDistrictData> cityDistrictDataList;

    @PostConstruct
    public void init() {
        try (InputStream inputStream = postalCodeResource.getInputStream()) {
            cityDistrictDataList = JsonUtils.readJson(inputStream, new TypeReference<>() {});
            cityDistrictDataList.forEach(city -> {
                city.getDistricts().forEach(district -> {
                    district.setZip(AddressValidator.converToFullWidth(district.getZip()));
                });
            });
            log.info("cityDistrictDataList : {}", JsonUtils.toPrettyJson(cityDistrictDataList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CityDistrictData> getAllCityDistrictData() {
        return cityDistrictDataList;
    }


    public static void printUnicode(String input) {
        for (char c : input.toCharArray()) {
            System.out.printf("%s (U+%04X)%n", c, (int)c);
        }
    }
}
