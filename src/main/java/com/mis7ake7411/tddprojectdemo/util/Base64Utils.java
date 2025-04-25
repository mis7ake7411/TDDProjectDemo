package com.mis7ake7411.tddprojectdemo.util;

import com.mis7ake7411.tddprojectdemo.model.base.Base64MultipartFile;
import com.mis7ake7411.tddprojectdemo.service.base.FileDownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class Base64Utils {
    private static final Pattern IMG_URL_PATTERN = Pattern.compile("<img[^>]+src=[\"'](https?://[^\"']+/image/display\\?[^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
    private static final Pattern IMG_BASE64_PATTERN = Pattern.compile("<img\\s+[^>]*src=[\"'](data:image/([a-zA-Z]+);base64,([A-Za-z0-9+/=]+))[\"']");

    private final FileDownloadService fileDownloadService;
    /**
     * 取得圖片的 Content-Type
     */
    public static String getImageContentType(String imageUrl) {
        if (imageUrl.endsWith(".jpg") || imageUrl.endsWith(".jpeg")) return "image/jpeg";
        if (imageUrl.endsWith(".png")) return "image/png";
        if (imageUrl.endsWith(".gif")) return "image/gif";
        if (imageUrl.endsWith(".tif") || imageUrl.endsWith(".tiff")) return "image/tiff";
        if (imageUrl.endsWith(".bmp")) return "image/bmp";
        if (imageUrl.endsWith(".webp")) return "image/webp";
        if (imageUrl.endsWith(".ico")) return "image/x-icon";
        if (imageUrl.endsWith(".svg")) return "image/svg+xml";
        if (imageUrl.endsWith(".heif") || imageUrl.endsWith(".heic")) return "image/heif";
        if (imageUrl.endsWith(".avif")) return "image/avif";
        return "application/octet-stream";
    }

    /**
     * 處理 HTML 內容，將含有 base64 的圖片轉換成附件 url
     *
     * @param uploadUrlHost 上傳檔案的 URL
     * @param tenantID    公司別
     * @param htmlContent HTML 內容
     * @return 處理後的 HTML 內容
     */
    public String processHtmlContent(String uploadUrlHost, String tenantID, String htmlContent)  {
        Matcher matcher = IMG_BASE64_PATTERN.matcher(htmlContent);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String fullMatch = matcher.group(1);  // Base64 整個 `src`
            String fileType = matcher.group(2);  // 取得副檔名（png, jpg, gif, ...）
            String base64Data = matcher.group(3); // 只有 Base64 純資料
            MultipartFile img = convertBase64ToMultipartFile(fileType, base64Data);
            String imageUrl = null;
            try {
                imageUrl = uploadFile(tenantID, uploadUrlHost, img.getOriginalFilename(), img.getInputStream());
            } catch (IOException e) {
                log.error("取得圖片 URL 失敗：{}，Base64={}...", fileType, base64Data.substring(0, Math.min(20, base64Data.length())));
            }
            if (imageUrl == null || imageUrl.isEmpty()) {
                log.warn("圖片上傳失敗，保留 Base64：{}", fileType);
                matcher.appendReplacement(result, matcher.group(0));
            } else {
                // 只替換 img Base64，不影響其他 HTML 內容
                matcher.appendReplacement(result, matcher.group(0).replace(fullMatch, imageUrl));
            }
        }
        // 保留 HTML 其他內容
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 將 Base64 字串轉換為 MultipartFile
     *
     * @param fileType   副檔名（png, jpg, gif,...）
     * @param base64Data Base64 字串
     * @return MultipartFile 物件
     */
    private MultipartFile convertBase64ToMultipartFile(String fileType, String base64Data){
        String contentType = "image/" + fileType;
        String fileName = "image_" + System.currentTimeMillis() + "." + fileType;
        // 轉換 Base64 字串為 byte[]
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        return new Base64MultipartFile(imageBytes, fileName, contentType);
    }

    /**
     * 上傳檔案
     *
     * @param tenantID 公司別
     * @param target 上傳檔案的 URL
     * @param fileName 檔案名稱
     * @param is       檔案串流
     * @return 檔案 URL
     */
    public String uploadFile(String tenantID, String target, String fileName, InputStream is) {
        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            //避免亂碼的編碼
            ContentType contentType = ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), StandardCharsets.UTF_8);
            //公司別
            multipartEntityBuilder.addTextBody("tenantID", tenantID, contentType);
            //檔案
            InputStreamBody contentBody = new InputStreamBody(is, fileName);
            multipartEntityBuilder.addPart("file", contentBody);

//            String result = httpSender.doPostByFile(target, fileName, multipartEntityBuilder.build());  // call web-agent upload image api
//            if (result == null) {
//                return "";
//            }
//            return Convert.toGsonObject(result)
//                    .get("dataList").getAsJsonArray()
//                    .get(0).getAsJsonObject()
//                    .get("src").getAsString();
            return "https://example.com/image/display?fileName=" + fileName; // 假設上傳成功，返回檔案 URL
        } catch (Exception e) {
            log.error("上傳檔案失敗");
        }
        return "";
    }

    /**
     * 轉換 HTML 內容中的 `<img src="URL/image/display?...">` 圖片為 Base64
     *
     * @param content HTML 內容
     * @return 轉換後的 HTML 內容
     */
    public String convertImageUrlsToBase64(String content) {
        log.info("轉換 HTML 內容中的圖片為 Base64 ");
        if (content.contains("&amp;")) {
            content = StringEscapeUtils.unescapeHtml4(content);
        }

        Matcher matcher = IMG_URL_PATTERN.matcher(content);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String imageUrl = matcher.group(1);
            log.info("發現圖片 URL：{}", imageUrl);
            //  下載並轉換為 Base64
            try {
                String base64Image = encodeFileToBase64FromUrl(imageUrl);
                if (base64Image == null) {
                    log.warn("無法將圖片轉換為 Base64：{}", imageUrl);
                    continue; // 跳過此圖片
                }
                // 取得 Content-Type
                String contentType = getImageContentType(imageUrl);
                base64Image = "data:" + contentType + ";base64," + base64Image;

                matcher.appendReplacement(result, matcher.group(0).replace(imageUrl, base64Image));
            } catch (Exception e) {
                log.error("圖片轉換失敗：{}", imageUrl);
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     *  從 url 取得檔案轉成 base64 字串
     *
     *  @param fileUrl  檔案 url
     *  @return base64 字串
     */
    public String encodeFileToBase64FromUrl(String fileUrl) {
        log.info("encodeFileToBase64FromUrl start fileUrl : {}", fileUrl);
        if (fileUrl == null || fileUrl.isEmpty()) {
            log.debug("fileUrl is null or empty");
            return null;
        }
        return fileDownloadService.encodeFileToBase64FromUrl(fileUrl);
    }
}
