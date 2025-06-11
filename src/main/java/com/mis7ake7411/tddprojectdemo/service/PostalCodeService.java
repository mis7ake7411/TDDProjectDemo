package com.mis7ake7411.tddprojectdemo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mis7ake7411.tddprojectdemo.model.CityDistrictData;
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
                    district.setZip(converseToFullWidth(district.getZip()));
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

    public static String converseToFullWidth(String input) {
        if (input == null) return null;

        // 先移除所有半形、全形空白
        String noSpaces = input.replaceAll("[\\s　]", "");

        // 再轉成全形
        StringBuilder sb = new StringBuilder();
        for (char c : noSpaces.toCharArray()) {
            if (c < 127) {
                sb.append((char)(c + 65248));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void printUnicode(String input) {
        for (char c : input.toCharArray()) {
            System.out.printf("%s (U+%04X)%n", c, (int)c);
        }
    }
}
