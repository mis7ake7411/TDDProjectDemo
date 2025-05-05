package com.mis7ake7411.tddprojectdemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {
    /** 分頁內容 */
    private  List<T> content;
    /** 當前頁碼 */
    private  int pageNumber;
    /** 每頁大小 */
    private  int pageSize;
    /** 總筆數 */
    private  long totalElements;
    /** 總頁數 */
    private  int totalPages;

    /**
     * 分頁回傳物件
     *
     * @param page Spring Data JPA 分頁物件
     */
    public PageResponseDTO(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber() + 1; // Spring Data JPA 預設從 0 開始, 回傳給前端時的頁碼需 +1
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    /**
     * 取得分頁
     *
     * @param pageNumber 頁碼
     * @param pageSize   每頁大小
     * @return 分頁
     */
    public static Pageable getPageable(int pageNumber, int pageSize) {
        return Pageable.ofSize(pageSize > 0 ? pageSize : 10) // 預設每頁 10 筆
                .withPage(Math.max(pageNumber - 1, 0)); // 前端頁碼從 1 開始, 轉換為從 0 開始
    }
}

