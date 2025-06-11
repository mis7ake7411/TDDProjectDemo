package com.mis7ake7411.tddprojectdemo.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AddressValidatorTest {

    @Test
    void testFullWidthConversion() {
        String input = "ABC123";
        String expected = "ＡＢＣ１２３";
        String result = AddressValidator.converToFullWidth(input);
        assertEquals(expected, result);
    }

    @Test
    void testFullWidthDetection() {
        assertTrue(AddressValidator.isFullWidth("ＡＢＣ１２３台北市"));
        assertFalse(AddressValidator.isFullWidth("ABC123台北市"));
    }

    @Test
    void testValidAddress() {
        // 這是完全正確、符合規範的全形地址
        String address = "台北市信義區信義路五段８巷３弄２０號Ｇ棟２０樓之１";
        assertTrue(AddressValidator.validateAddress(address));
    }

    @Test
    void testInvalidAddress_halfWidthNumbers() {
        // 半形數字
        String address = "台北市信義區信義路五段8巷3弄20號Ｇ棟20樓之一";
        assertFalse(AddressValidator.validateAddress(address));
    }

    @Test
    void testInvalidAddress_halfWidthLetters() {
        // 半形英文
        String address = "台北市信義區信義路五段８巷３弄２０號G樓２０棟之一";
        assertFalse(AddressValidator.validateAddress(address));
    }

    @Test
    void testInvalidAddress_spaceIncluded() {
        // 包含空白
        String address = " 台北市信義區 信義路五段８巷３弄２０號Ｇ棟２０樓之１ ";
        assertTrue(AddressValidator.validateAddress(address)); // 會被 normalize 後還是可通過
    }

    @Test
    void testInvalidAddress_missingDashConversion() {
        // - 未轉換成 "之"
        String address = "台北市信義區信義路五段８巷３弄２０號Ｇ棟２０樓-1";
        assertFalse(AddressValidator.validateAddress(address));
    }

    @Test
    void testInvalidAddress_wrongSegmentOrder() {
        // 結構混亂
        String address = "台北市信義路２０號五段３弄８巷Ｇ棟２０樓之一";
        assertFalse(AddressValidator.validateAddress(address));
    }
}