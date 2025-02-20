package com.mis7ake7411.tddprojectdemo.service;

import com.mis7ake7411.tddprojectdemo.model.HavingCriteria;
import com.mis7ake7411.tddprojectdemo.model.SearchCriteria;
import com.mis7ake7411.tddprojectdemo.model.SortCriteria;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 通用查詢 Service
 */
public interface GenericSearchService {

  /**
   * 通用的動態查詢方法
   *
   * @param <T> 實體類型
   * @param repository JPA Repository
   * @param criteriaList 查詢條件列表
   * @param pageable 分頁資訊
   * @return 分頁結果
   */
  public <T> Page<T> dynamicSearch(JpaSpecificationExecutor<T> repository,
      List<SearchCriteria<?>> criteriaList,
      Pageable pageable);

  /**
   * 通用的動態查詢方法, 支援 WHERE、ORDER BY
   *
   * @param <T> 實體類型
   * @param repository JPA Repository
   * @param criteriaList 查詢條件列表
   * @param orderByList 排序條件列表
   * @param pageable 分頁資訊
   * @return 分頁結果
   */
  public <T> Page<T> dynamicSearchWithOrderBy(JpaSpecificationExecutor<T> repository,
      List<SearchCriteria<?>> criteriaList,
      List<SortCriteria> orderByList,
      Pageable pageable);

  /**
   * 通用的動態查詢方法, 支援 WHERE、ORDER BY、GROUP BY
   *
   * @param <T> 實體類型
   * @param repository JPA Repository
   * @param criteriaList 查詢條件
   * @param orderByList 排序條件
   * @param groupByList group by 欄位
   * @param pageable 分頁資訊
   * @return 分頁結果
   */
  public <T> Page<T> dynamicSearchWithGroupBy(JpaSpecificationExecutor<T> repository,
      List<SearchCriteria<?>> criteriaList,
      List<SortCriteria> orderByList,
      List<String> groupByList,
      Pageable pageable);

  /**
   * 通用的動態查詢方法, 支援 WHERE、ORDER BY、GROUP BY、HAVING
   *
   * @param <T> 實體類型
   * @param repository JPA Repository
   * @param criteriaList 查詢條件
   * @param orderByList 排序條件
   * @param groupByList group by 欄位
   * @param havingList having 條件
   * @param pageable 分頁資訊
   * @return 分頁結果
   */
  public <T> Page<T> dynamicSearchWithHaving(JpaSpecificationExecutor<T> repository,
      List<SearchCriteria<?>> criteriaList,
      List<SortCriteria> orderByList,
      List<String> groupByList,
      List<HavingCriteria<?>> havingList,
      Pageable pageable);
}
