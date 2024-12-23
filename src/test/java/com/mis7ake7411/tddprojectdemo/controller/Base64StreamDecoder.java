package com.mis7ake7411.tddprojectdemo.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Base64StreamDecoder {
    public static void main(String[] args) throws Exception {
        File base64File = new File("C:\\Users\\Allen Wu\\Downloads\\base64Image.txt");
        File outFile = new File("C:\\Users\\Allen Wu\\Downloads\\output.png");
        // 打開 Base64 文件（包含編碼數據）
//        InputStream base64InputStream = Files.newInputStream(Paths.get("base64Image.txt"));
        InputStream base64InputStream = Files.newInputStream(Paths.get("base64Image.txt"));

        // 創建解碼後的輸出文件
//        FileOutputStream imageOutputStream = new FileOutputStream("output.png");
        FileOutputStream imageOutputStream = new FileOutputStream("output.png");

        // 使用 Base64 解碼
        Base64.Decoder decoder = Base64.getMimeDecoder();
        InputStream decodedStream = decoder.wrap(base64InputStream);

        // 讀取並寫入圖片文件
        byte[] buffer = new byte[8192];  // 每次讀取 8KB
        int bytesRead;
        while ((bytesRead = decodedStream.read(buffer)) != -1) {
            imageOutputStream.write(buffer, 0, bytesRead);
        }

        // 關閉流
        base64InputStream.close();
        imageOutputStream.close();

        System.out.println("圖片已成功轉換並保存。");
    }
}