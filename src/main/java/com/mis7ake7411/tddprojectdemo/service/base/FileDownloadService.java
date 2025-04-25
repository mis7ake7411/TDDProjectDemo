package com.mis7ake7411.tddprojectdemo.service.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileDownloadService {

    private final WebClient webClient;

    /**
     * 從 URL 下載檔案並轉為 Base64 字串
     *
     * @param fileUrl 檔案 URL
     * @return Base64 字串，或 null 表示錯誤
     */
    public String encodeFileToBase64FromUrl(String fileUrl) {
        log.info("encodeFileToBase64FromUrl start, fileUrl: {}", fileUrl);

        if (fileUrl == null || fileUrl.isEmpty()) {
            log.debug("fileUrl is null or empty");
            return null;
        }

        try {
            byte[] fileBytes = webClient.get()
                    .uri(fileUrl)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();  // 同步等結果

            if (fileBytes == null || fileBytes.length == 0) {
                log.debug("fileBytes length is 0");
                return null;
            }

            log.info("fileBytes length: {}", fileBytes.length);
            return Base64.getEncoder().encodeToString(fileBytes);

        } catch (Exception e) {
            log.error("encodeFileToBase64FromUrl error: {}", e.getMessage());
            return null;
        }
    }
}