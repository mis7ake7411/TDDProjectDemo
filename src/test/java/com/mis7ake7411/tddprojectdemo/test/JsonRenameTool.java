package com.mis7ake7411.tddprojectdemo.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public class JsonRenameTool {
    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // 讀取 classpath 中的檔案
        Resource resource = new ClassPathResource("json/taiwanDistricts_zh-TW.json");
        try (InputStream inputStream = resource.getInputStream()) {
            List<Map<String, Object>> cities = objectMapper.readValue(inputStream, new TypeReference<>() {});

            // 逐筆轉換
            for (Map<String, Object> city : cities) {
                Object name = city.remove("name");
                city.put("cityName", name);

                List<Map<String, Object>> districts = (List<Map<String, Object>>) city.get("districts");
                for (Map<String, Object> district : districts) {
                    Object dName = district.remove("name");
                    district.put("districtName", dName);
                }
            }

            // 輸出到「專案根目錄」的新檔案（非 classpath！）
            File outputFile = new File(System.getProperty("user.home") + "\\Downloads\\" + "taiwanDistricts_zh-TW-renamed.json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, cities);

            System.out.println("轉換完成！輸出檔案：" + outputFile.getAbsolutePath());
        }
    }
}

