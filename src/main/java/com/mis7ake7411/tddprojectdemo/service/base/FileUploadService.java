package com.mis7ake7411.tddprojectdemo.service.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private final WebClient webClient;

    /**
     * 上傳檔案 (multipart/form-data)
     *
     * @param uploadUrl API 接收的 URL
     * @param fileName 欲送出的檔案欄位名（通常是 "file"）
     * @param file 檔案實體
     * @param headers 可選自訂 Header（如 Authorization）
     * @return 回傳結果字串（可自定解析）
     */
    public String uploadFile(String uploadUrl, String fileName, File file, Map<String, String> headers) {
        log.info("Start uploading file to {}", uploadUrl);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add(fileName, new FileSystemResource(file));

        WebClient.RequestBodySpec request = webClient.post()
                .uri(uploadUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        if (headers != null) {
            headers.forEach(request::header);
        }

        return request
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * 上傳 byte[] 為檔案（通常 API 要 raw stream）
     */
    public String uploadRawBytes(String uploadUrl, byte[] bytes, Map<String, String> headers) {
        log.info("Uploading raw bytes to {}", uploadUrl);

        WebClient.RequestBodySpec request = webClient.post()
                .uri(uploadUrl)
                .contentType(MediaType.APPLICATION_OCTET_STREAM);

        if (headers != null) {
            headers.forEach(request::header);
        }

        return request
                .bodyValue(bytes)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * 上傳 base64 字串為檔案
     */
    public String uploadBase64Files(
            String uploadUrl,
            Map<String, String> base64Files,  // key: 欄位名（如 "file1"），value: base64 字串
            Map<String, String> headers       // 可選 headers
    ) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

        base64Files.forEach((fieldName, base64String) -> {
            byte[] fileBytes = Base64.getDecoder().decode(base64String);

            ByteArrayResource resource = new ByteArrayResource(fileBytes) {
                @Override
                public String getFilename() {
                    return fieldName + ".bin"; // 可改為真實檔名
                }
            };

            formData.add(fieldName, resource);
        });

        WebClient.RequestBodySpec request = webClient.post()
                .uri(uploadUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        if (headers != null) {
            headers.forEach(request::header);
        }

        return request
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}

