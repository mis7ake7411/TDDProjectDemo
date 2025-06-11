package com.mis7ake7411.tddprojectdemo.util;

import java.util.regex.Pattern;

public class AddressValidator {

    /**
     * 地址欄位標準化：移除空白，半形轉全形
     */
    public static String converToFullWidth(String input) {
        if (input == null) return null;

        // 先移除所有半形、全形空白
        String noSpaces = input.replaceAll("[\\s　]", "");

        // 半形轉全形
        StringBuilder sb = new StringBuilder();
        for (char c : noSpaces.toCharArray()) {
            if (c >= 33 && c <= 126) { // 半形可見字元
                sb.append((char) (c + 65248));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 檢核是否全形字元（中文、全形英數/符號、全形空白）
     */
    public static boolean isFullWidth(String s) {
        for (char c : s.toCharArray()) {
            if (c == '\u3000' || (c >= '\uFF01' && c <= '\uFF5E') || (c >= '\u4E00' && c <= '\u9FFF')) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 地址結構檢核
     */
    public static boolean validateAddress(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }

        // 1️. 全形化轉換 + 移除空白
        address = converToFullWidth(address);

        // 2. 是否全部全形
        if (!isFullWidth(address)) {
            System.out.println(" 非全形字元存在！");
            return false;
        }

        // 3. "-" 轉成 "之"
        address = address.replace("-", "之");

        // 4. 正則匹配檢核
        Pattern duanPattern = Pattern.compile("[一二三四五六七八九十百]+段");
        Pattern numericPattern = Pattern.compile("[０-９]+([里鄰巷弄號樓])");
        Pattern buildingPattern = Pattern.compile("[Ａ-Ｚ]+棟");
        Pattern louZhiPattern = Pattern.compile("[０-９]+樓之[０-９]+");

        // 可逐段匹配或用 find() 檢核出現次數
        if (!duanPattern.matcher(address).find()) {
            System.out.println(" 段格式不符！");
            return false;
        }

        if (!numericPattern.matcher(address).find()) {
            System.out.println(" 巷/弄/號 格式不符！");
            return false;
        }

        if (!buildingPattern.matcher(address).find()) {
            System.out.println(" 棟 格式不符！");
            return false;
        }

        if (!louZhiPattern.matcher(address).find()) {
            System.out.println(" 樓之 格式不符！");
            return false;
        }

        return true;
    }
}
