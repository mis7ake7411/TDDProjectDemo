package com.mis7ake7411.tddprojectdemo.service;

import com.mis7ake7411.tddprojectdemo.model.SearchCriteria;
import com.mis7ake7411.tddprojectdemo.model.SortCriteria;
import com.mis7ake7411.tddprojectdemo.util.SpecificationBuilderUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public class GenericSearchService {

    /**
     * 通用的動態查詢方法
     *
     * @param <T> 實體類型
     * @param repository JPA Repository
     * @param criteriaList 查詢條件列表
     * @param orderByList 排序條件列表
     * @param pageable 分頁資訊
     * @return 分頁結果
     */
    public static <T> Page<T> dynamicSearch(JpaSpecificationExecutor<T> repository,
                                            List<SearchCriteria<?>> criteriaList,
                                            List<SortCriteria> orderByList,
                                            Pageable pageable) {

        // 使用 SpecificationBuilder 構建 Specification
        Specification<T> specification = SpecificationBuilderUtils.build(criteriaList, orderByList);

        // 執行查詢
        return repository.findAll(specification, pageable);
    }
}

