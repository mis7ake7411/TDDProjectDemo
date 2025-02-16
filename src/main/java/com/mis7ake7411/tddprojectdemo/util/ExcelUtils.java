package com.mis7ake7411.tddprojectdemo.util;

import static org.apache.poi.ss.usermodel.BorderStyle.THIN;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    /**
     * 創建Workbook
     * 
     * @param  in InputStream
     * @return Workbook
     * @throws Exception exception
     */
    public static Workbook createWorkBook(InputStream in) throws Exception {
        try {
          XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
          return new SXSSFWorkbook(xssfWorkbook);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 獲取單儲存格字串值
     * 
     * @param cell
     * @return String
     */
    public static String getCellStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        cell.setCellType(CellType.STRING);
        RichTextString str = cell.getRichStringCellValue();
        return str.getString();
    }

    /**
     * 初始化Excel儲存格, 設置儲存格值和樣式
     * 
     * @param cell
     * @param style
     */
    public static void initCell(Cell cell, CellStyle style, String value) {
        cell.setCellStyle(style);
        cell.setCellValue(value);
    }
    
    /**
     * 初始化Excel儲存格, 設置儲存格值和樣式
     * 
     * @param row
     * @param style
     * @param datas
     */
    public static void initCell(Row row, CellStyle style, String[] datas) {
      for(int i = 0; i < datas.length; i++) {
        Cell cell = row.createCell(i);
        cell.setCellStyle(style);
        cell.setCellValue(datas[i]);
      }
    }  
    
    /**
     * 初始化Excel儲存格, 設置儲存格值、樣式和備註
     * 
     * @param cell
     * @param style
     * @param value
     * @param 
     */
    public static void initCell(Cell cell, CellStyle style, String value, Comment comment) {
        cell.setCellStyle(style);
        cell.setCellValue(value);
        cell.setCellComment(comment);
    }

    /**
     * 獲取Excel儲存格備註
     * 
     * @param drawing
     * @param anchor
     * @param content
     * @return Comment
     */
    public static Comment getCellComment(Drawing drawing, HSSFClientAnchor anchor, String content) {
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(new HSSFRichTextString(content));
        return comment;
    }

    /**
     * 獲取Excel標題儲存格樣式
     * 
     * @param wb
     * @return CellStyle
     */
    public static CellStyle getHeadStyle(Workbook wb) {
        Font font = wb.createFont();
        font.setBold(true); // 粗體
        font.setFontHeightInPoints((short) 12);

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        style.setLocked(true);
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }

    /**
     * 獲取Excel資料儲存格樣式
     * 
     * @param wb
     * @return CellStyle
     */
    public static CellStyle getBodyStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderTop(THIN);
        style.setBorderRight(THIN);
        style.setBorderBottom(THIN);
        style.setBorderLeft(THIN);
        return style;
    }

    /**
     * 獲取Excel錯誤儲存格樣式
     * 
     * @param  wb
     * @return CellStyle
     */
    public static CellStyle getErrorStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();

        Font font = wb.createFont();
        font.setColor((short) 10); // 紅色
        style.setFont(font);
        return style;
    }

    /**
     * 獲取Excel下拉選單樣式
     * 
     * @param wb
     * @return CellStyle
     */
    public static CellStyle getSelectStyle(Workbook wb) {
      CellStyle style = wb.createCellStyle();
      style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
      style.setAlignment(HorizontalAlignment.CENTER);
      style.setWrapText(true);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      return style;
    }
    
    /**
     * @param wb HSSFWorkbook對象
     * @param nameName 
     * @param formula 
     * @return
     */
    private static Name createName(Workbook wb, String nameName, String formula) {
      Name name = wb.createName();
      name.setNameName(nameName);
      name.setRefersToFormula(formula);
      return name;
    }

    /**
     * 不可數字開頭
     *
     * @param name
     * @return
     */
    public static String formatName(String name) {
      name = name.replaceAll(" ", "").replaceAll("-", "_").replaceAll(":", ".");
      if (Character.isDigit(name.charAt(0))) {
        name = "_" + name;
      }
      return name;
    }
    
    /**
     * 轉換Excel欄位列title字母
     *
     * @param columnNumber
     * @return
     */
    public static String formatColumnName(int columnNumber) {
      StringBuilder columnLetter = new StringBuilder();
      while (columnNumber >= 0) {
        int remainder = columnNumber % 26;
        columnLetter.insert(0, (char) ('A' + remainder));
        columnNumber = (columnNumber / 26) - 1;
      }
      return columnLetter.toString();
    }
    
    
    /**
     * 取得Excel欄位列index
     *
     * @param columnName
     * @return
     */
    public static int convertColumnName(String columnName) {
      char[] charArr = columnName.toCharArray();
      int power = 1; // 初始平方值
      int index = 0;
      
      for (int i = charArr.length - 1; i >= 0; i--) {
        index += (Character.toUpperCase(charArr[i]) - 'A' + 1) * power; // 使用26進位轉換
        power *= 26; // 每次迴圈結束後，次方值乘以 26
      }
      return index;
    }
    
    /**
     * 超過big5編碼範圍的文字轉換成NCR字串
     *
     * @param input
     * @return
     */
    public static String convertExceedBig5ToNCR(String input) {
      String encoding = "Big5";
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < input.length(); i++) {
        char c = input.charAt(i);
        byte[] bytes = Character.toString(c)
                                .getBytes(Charset.forName(encoding));
        String decodedString = new String(bytes, Charset.forName(encoding));
        if(!Character.toString(c)
                     .equals(decodedString)) {
          builder.append("&#")
                 .append((int) c)
                 .append(";");
        }else {
          builder.append(c);
        }
      }
      return builder.toString();
    }
    
    /**
     * NCR字串轉換成java unicode文字
     *
     * @param input
     * @return
     */    
    public static String convertToJavaUnicodeFromNCR(String input) {
      StringBuilder builder = new StringBuilder();
      int lastIndex = 0;
      while(lastIndex < input.length()){
        int startIndex = input.indexOf("&#", lastIndex);
        if(startIndex == -1) {
          // 沒有找到 NCR 字符，直接添加剩餘的字串
          builder.append(input.substring(lastIndex));
          break;
        }
        
        int endIndex = input.indexOf(";", startIndex);
        if(endIndex == -1) {
          // NCR 格式不正確，直接添加剩餘的字串
          builder.append(input.substring(endIndex));
          break;
        }
        
        // 加入的非 NCR 字串部分
        builder.append(input, lastIndex, startIndex);
        // 取得unicode
        String ncrValue = input.substring(startIndex + 2, endIndex);
        try {
          int codePoint = Integer.parseInt(ncrValue);
          builder.appendCodePoint(codePoint);
        } catch (NumberFormatException e) {
          // NCR 值不是有效的整數，直接添加剩餘的字串
          builder.append(input, lastIndex, endIndex + 1);
        } 
        lastIndex = endIndex + 1;
      }
      return builder.toString();
    }
    
    /**
     * 給sheet頁，添加下拉清單
     *
     * @param wb    excel文件，用於添加Name
     * @param targetSheet 級聯列表所在sheet頁
     * @param options     級聯數據 
     * @param column      下拉清單所在列 從'A'開始
     * @param fromRow     下拉限制開始行
     * @param endRow      下拉限制結束行
     * @param hiddenSheetIndex 隱藏的sheet索引
     */
    public static void addValidationToSheet(Workbook wb, Sheet targetSheet, Object[] options, char column, int fromRow, int endRow, int hiddenSheetIndex) {
        XSSFWorkbook workbook = (XSSFWorkbook) wb;
        String hiddenSheetName = "sheet" + workbook.getNumberOfSheets();
        // 創建一個資料來源sheet
        Sheet optionsSheet = workbook.createSheet(hiddenSheetName);
        // 資料來源sheet頁不顯示
        workbook.setSheetHidden(hiddenSheetIndex, true);
        // 將下拉清單的數據放在資料來源sheet上
        String nameName = column + "_parent";
        int rowIndex = 0;
        for (Object option : options) {
            int columnIndex = 0;
            Row row = optionsSheet.createRow(rowIndex++);
            Cell cell = row.createCell(columnIndex++);
            cell.setCellValue(option.toString());
        }

        createName(workbook, nameName, hiddenSheetName + "!$A$1:$A$" + options.length);

        DVConstraint constraint = DVConstraint.createFormulaListConstraint(nameName);
        CellRangeAddressList regions = new CellRangeAddressList(fromRow, endRow, (int) column - 'A', (int) column - 'A');
        targetSheet.addValidationData(new HSSFDataValidation(regions, constraint));
    }

    /**
     * 給sheet頁 添加級聯下拉清單
     *
     * @param wb excel
     * @param targetSheet sheet頁
     * @param options 要添加的下拉清單內容 ， keys 是下拉清單1中的內容，每個Map.Entry.Value 是對應的級聯下拉清單內容
     * @param keyColumn 下拉清單1位置
     * @param valueColumn 級聯下拉清單位置
     * @param fromRow 級聯限制開始行
     * @param endRow 級聯限制結束行
     */
    public static void addValidationToSheet(Workbook wb, Sheet targetSheet, Map<String, List<String>> options,
                                            char keyColumn, char valueColumn, int fromRow, int endRow, int hiddenSheetIndex) {
      XSSFWorkbook workbook = (XSSFWorkbook) wb;
      // 創建一個資料來源sheet
      String hiddenSheetName = "sheet" + workbook.getNumberOfSheets();
      Sheet hiddenSheet = workbook.createSheet(hiddenSheetName);
      // 資料來源sheet頁不顯示
      workbook.setSheetHidden(hiddenSheetIndex, true);
      List<String> firstLevelItems = new ArrayList<>();

      int rowIndex = 0;
      for (Entry<String, List<String>> entry : options.entrySet()) {
        String parent = formatName(entry.getKey());
        firstLevelItems.add(parent);
        List<String> children = entry.getValue();

        int columnIndex = 0;
        Row row = hiddenSheet.createRow(rowIndex++);
        Cell cell = null;

        for (String child : children) {
          cell = row.createCell(columnIndex++);
          cell.setCellValue(child);
        }

        String lastChildrenColumn = formatColumnName(children.size() - 1);
        createName(workbook, parent, String.format(hiddenSheetName + "!$A$%s:$%s$%s", rowIndex, lastChildrenColumn, rowIndex));

        DVConstraint constraint = DVConstraint.createFormulaListConstraint("INDIRECT($" + keyColumn + "1)");
        CellRangeAddressList regions = new CellRangeAddressList(fromRow, endRow, valueColumn - 'A', valueColumn - 'A');
        targetSheet.addValidationData(new HSSFDataValidation(regions, constraint));
      }
      
      // for 子選單index
      hiddenSheetIndex++;
      addValidationToSheet(workbook, targetSheet, firstLevelItems.toArray(), keyColumn, fromRow, endRow, hiddenSheetIndex);

    }
    
    /**
     * 根據使用者在keyColumn選擇的key, 自動填充value到valueColumn
     *
     * @param wb    excel
     * @param targetSheet sheet頁
     * @param keyValues   匹配關係 
     * @param keyColumn   要匹配的列
     * @param valueColumn 匹配到的內容列
     * @param fromRow     下拉限制開始行
     * @param endRow      下拉限制結束行
     * @param hiddenSheetIndex 隱藏的sheet索引
     */
    public static void addAutoMatchValidationToSheet(Workbook wb, Sheet targetSheet, Map<String, Object> keyValues, 
                                                     char keyColumn, char valueColumn, int fromRow, int endRow, int hiddenSheetIndex) {
        XSSFWorkbook workbook = (XSSFWorkbook) wb;
        // 創建一個資料來源sheet
        String hiddenSheetName = "sheet" + workbook.getNumberOfSheets();
        Sheet hiddenSheet = workbook.createSheet(hiddenSheetName);
        // 資料來源sheet頁不顯示
        workbook.setSheetHidden(hiddenSheetIndex, true);
        // init the search region(A and B columns in hiddenSheet)
        int rowIndex = 0;
        for (Entry<String, Object> kv : keyValues.entrySet()) {
            Row totalSheetRow = hiddenSheet.createRow(rowIndex++);

            Cell cell = totalSheetRow.createCell(0);
            cell.setCellValue(kv.getKey());

            cell = totalSheetRow.createCell(1);
            if(kv.getValue() instanceof String) {
              cell.setCellValue(kv.getValue().toString());
            }else if(kv.getValue() instanceof Date) {
              cell.setCellValue((Date)kv.getValue());
            }else if(kv.getValue() instanceof Integer) {
              cell.setCellValue((Integer)kv.getValue());
            }else if(kv.getValue() instanceof Double) {
              cell.setCellValue((Double)kv.getValue());
            }
        }

        for (int i = fromRow; i <= endRow; i++) {
            Row totalSheetRow = targetSheet.getRow(i);
            if (totalSheetRow == null) {
                totalSheetRow = targetSheet.createRow(i);
            }

            Cell cell = totalSheetRow.getCell((int) valueColumn - 'A');
            if (cell == null) {
                cell = totalSheetRow.createCell((int) valueColumn - 'A');
            }

            String keyCell = String.valueOf(keyColumn) + (i + 1);
            String formula = String.format("IF(ISNA(VLOOKUP(%s,%s!A:B,2,0)),\"\",VLOOKUP(%s,%s!A:B,2,0))", keyCell, hiddenSheetName, keyCell, hiddenSheetName);

            cell.setCellFormula(formula);
        }
        // for 子選單index
        hiddenSheetIndex++;
        // init the keyColumn as comboList
        addValidationToSheet(workbook, targetSheet, keyValues.keySet().toArray(), keyColumn, fromRow, endRow, hiddenSheetIndex);
    }

    
    /**
     * @param wb HSSFWorkbook對象
     * @param realSheet 需要操作的sheet物件
     * @param datas 下拉的清單數據
     * @param startRow 開始行
     * @param endRow 結束行
     * @param startCol 開始列
     * @param endCol 結束列
     * @param hiddenSheetName 隱藏的sheet名
     * @param hiddenSheetIndex 隱藏的sheet索引
     */
    public static void addValidationToSheet(Workbook wb, Sheet realSheet, Object[] datas, int startRow, int endRow, 
                                            int startCol, int endCol, String hiddenSheetName, int hiddenSheetIndex) throws Exception {
      XSSFWorkbook workbook = (XSSFWorkbook) wb;
      // 創建一個資料來源sheet
      Sheet hidden = workbook.createSheet(hiddenSheetName);
      // 資料來源sheet頁不顯示
//      workbook.setSheetHidden(hiddenSheetIndex, true);
      // 將下拉清單的數據放在資料來源sheet上
      Row row = null;
      Cell cell = null;

      for (int i = 0, length = datas.length; i < length; i++) {
          row = hidden.createRow(i);
          cell = row.createCell(0);
          if(datas[i] instanceof String) {
            cell.setCellValue((String) datas[i]);
          }else if(datas[i] instanceof Integer) {
            cell.setCellValue((int) datas[i]);
          }else if(datas[i] instanceof Date) {
            cell.setCellValue((Date) datas[i]);
          }
      }
      //HSSFName namedCell = workbook.createName();
      //namedCell.setNameName(hiddenSheetName);
      // A1 到 Adatas.length 表示第一列的第一行到datas.length行，需要與前一步生成的隱藏的資料來源sheet資料位置對應
      //namedCell.setRefersToFormula(hiddenSheetName + "!$A$1:$A" + datas.length);
      // 指定下拉資料時，給定目標資料範圍 hiddenSheetName!$A$1:$A5   隱藏sheet的A1到A5格的數據
      DVConstraint constraint = DVConstraint.createFormulaListConstraint(hiddenSheetName + "!$A$1:$A" + datas.length);
      CellRangeAddressList addressList = null;
      HSSFDataValidation validation = null;
      row = null;
      cell = null;
      // 儲存格樣式
      CellStyle style = getSelectStyle(wb);
      // 迴圈指定儲存格下拉資料
      for (int i = startRow; i <= endRow; i++) {
          row = (HSSFRow) realSheet.createRow(i);
          cell = row.createCell(startCol);
          cell.setCellStyle(style);
          addressList = new CellRangeAddressList(i, i, startCol, endCol);
          validation = new HSSFDataValidation(addressList, constraint);
          realSheet.addValidationData(validation);
      }
    }
    
}

