package com.mis7ake7411.tddprojectdemo.controller;

import com.mis7ake7411.tddprojectdemo.model.CityDistrictData;
import com.mis7ake7411.tddprojectdemo.service.PostalCodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class PostalCodeController {

    private final PostalCodeService postalCodeService;

    public PostalCodeController(PostalCodeService postalCodeService) {
        this.postalCodeService = postalCodeService;
    }

    @GetMapping("/postalCode")
    public List<CityDistrictData> getAllPostalCodes() {
        return postalCodeService.getAllCityDistrictData();
    }
}

