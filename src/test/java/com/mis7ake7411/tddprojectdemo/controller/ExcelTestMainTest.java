package com.mis7ake7411.tddprojectdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ExcelTestMainTest {

    public static void main(String[] args) {
        int dataSize = 10000; // 測試 10,000 筆資料
        int columnSize = 15; // 欄位數量
        List<TestDataVO> dataList = generateTestData(dataSize);
        List<Function<TestDataVO, String>> fieldExtractorList = generateFieldExtractors(columnSize);

        try(XSSFWorkbook workbook = new XSSFWorkbook();) {
            Sheet sheet1 = workbook.createSheet("原始版本");
            Sheet sheet2 = workbook.createSheet("優化版本");


            log.info("開始測試原始版本...");
            long startTime1 = System.currentTimeMillis();
            fillDataRows(sheet1, dataList, fieldExtractorList);
            long endTime1 = System.currentTimeMillis();
            log.info("原始版本執行時間: {}ms", endTime1 - startTime1);

            log.info("開始測試優化版本...");
            long startTime2 = System.currentTimeMillis();
            fillDataRowsOptimized(sheet2, dataList, fieldExtractorList);
            long endTime2 = System.currentTimeMillis();
            log.info("優化版本執行時間: {}ms", endTime2 - startTime2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  原始版本（巢狀迴圈，計算量大）**
     */
    private static <T> void fillDataRows(Sheet sheet, List<T> dataList,
                                         List<Function<T, String>> fieldExtractorList) {
        int applyCallCount = 0;
        int setCellCount = 0;
        for (int i = 0; i < dataList.size(); i++) {
            T data = dataList.get(i);
            Row row = sheet.createRow(i + 2);
            row.setHeight((short) 500);

            for (int col = 0; col < fieldExtractorList.size(); col++) {
                applyCallCount++;
                setCellCount++;
                setCellValue(row, col, fieldExtractorList.get(col).apply(data));
            }
        }
        log.info(" [原始版本] apply(data) 總執行次數: {}", applyCallCount);
        log.info(" [原始版本] setCellValue() 總執行次數: {}", setCellCount);
    }

    /**
     * 優化版本（stream() 預計算，減少 apply(data) 呼叫）**
     */
    private static <T> void fillDataRowsOptimized(Sheet sheet, List<T> dataList,
                                                  List<Function<T, String>> fieldExtractorList) {
        int[] applyCallCount = {0};
        int setCellCount = 0;
        int rowIndex = 2; // 從 Excel 第 2 行開始
        for (T data : dataList) {
            Row row = sheet.createRow(rowIndex++);
            row.setHeight((short) 500);

            // 預先計算所有欄位的值
            List<String> rowValues = fieldExtractorList.stream()
                    .map(extractor -> {
                        applyCallCount[0]++;
                        return extractor.apply(data);
                    })
                    .collect(Collectors.toList());

            //一次性填入所有欄位值
            for (int col = 0; col < rowValues.size(); col++) {
                setCellCount++;
                setCellValue(row, col, rowValues.get(col));
            }
        }
        log.info(" [優化版本] apply(data) 總執行次數: {}", applyCallCount[0]);
        log.info(" [優化版本] setCellValue() 總執行次數: {}", setCellCount);
    }

    /**
     * 優化版本（stream() 預計算,， 使用 Map **
     */
    private static <T> void fillDataRowsOptimizedUseMap(Sheet sheet, List<T> dataList,
                                                        List<Function<T, String>> fieldExtractorList) {
        int applyCallCount = 0;
        int setCellCount = 0;
        int mapCreateCount = 0; // 記錄 Map 被建立的次數
        int rowIndex = 2; // 從 Excel 第 2 行開始填充資料

        for (T data : dataList) {
            Map<Integer, String> rowValues = new HashMap<>();
            mapCreateCount++; // 記錄 Map 建立次數

            for (int col = 0; col < fieldExtractorList.size(); col++) {
                rowValues.put(col, fieldExtractorList.get(col).apply(data));
                applyCallCount++;
            }

            Row row = sheet.createRow(rowIndex++);
            row.setHeight((short) 500);

            for (int col = 0; col < fieldExtractorList.size(); col++) {
                setCellCount++;
                setCellValue(row, col, rowValues.get(col));
            }
        }
        log.info(" [優化版本_Map] apply(data) 總執行次數: {}", applyCallCount);
        log.info(" [優化版本_Map] setCellValue() 總執行次數: {}", setCellCount);
        log.info(" [優化版本_Map] Map 總建立次數: {}", mapCreateCount);
    }

    /**
     * 假資料物件
     */
    static class TestDataVO {
        private final String[] fields;

        public TestDataVO(String[] fields) {
            this.fields = fields;
        }

        public String getField(int index) {
            return fields[index];
        }
    }

    /**
     * 產生測試數據**
     */
    private static List<TestDataVO> generateTestData(int size) {
        List<TestDataVO> dataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String[] fields = new String[15]; // 15 個欄位
            for (int j = 0; j < fields.length; j++) {
                fields[j] = "Data_" + i + "_" + j;
            }
            dataList.add(new TestDataVO(fields));
        }
        return dataList;
    }

    /**
     * 產生動態欄位提取函式**
     */
    private static List<Function<TestDataVO, String>> generateFieldExtractors(int columnSize) {
        List<Function<TestDataVO, String>> extractors = new ArrayList<>();
        for (int i = 0; i < columnSize; i++) {
            final int index = i; // 需為 final 或 effectively final
            extractors.add(data -> data.getField(index));
        }
        return extractors;
    }

    /**
     * **模擬 setCellValue 寫入 Excel**
     */
    private static void setCellValue(Row row, int columnIndex, String value) {
        row.createCell(columnIndex).setCellValue(value);
    }
}

