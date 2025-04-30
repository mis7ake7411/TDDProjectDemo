package com.mis7ake7411.tddprojectdemo.service.base.Impl;

import com.mis7ake7411.tddprojectdemo.model.HavingCriteria;
import com.mis7ake7411.tddprojectdemo.model.SearchCriteria;
import com.mis7ake7411.tddprojectdemo.model.SortCriteria;
import com.mis7ake7411.tddprojectdemo.service.base.GenericSearchService;
import com.mis7ake7411.tddprojectdemo.util.SpecificationBuilderUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GenericSearchServiceImpl implements GenericSearchService {

    public <T> Page < T > dynamicSearch(JpaSpecificationExecutor < T > repository,
        List < SearchCriteria < ? > > criteriaList,
        Pageable pageable) {

        // 使用 SpecificationBuilder 構建 Specification
        Specification < T > specification = SpecificationBuilderUtils.build(criteriaList);

        // 執行查詢
        return repository.findAll(specification, pageable);
    }

    public <T> Page<T> dynamicSearchWithOrderBy(JpaSpecificationExecutor<T> repository,
                                            List<SearchCriteria<?>> criteriaList,
                                            List<SortCriteria> orderByList,
                                            Pageable pageable) {

        // 使用 SpecificationBuilder 構建 Specification
        Specification<T> specification = SpecificationBuilderUtils.build(criteriaList, orderByList);

        // 執行查詢
        return repository.findAll(specification, pageable);
    }

    public <T> Page<T> dynamicSearchWithGroupBy(JpaSpecificationExecutor<T> repository,
                                            List<SearchCriteria<?>> criteriaList,
                                            List<SortCriteria> orderByList,
                                            List<String> groupByList,
                                            Pageable pageable) {

        // 使用 SpecificationBuilder 構建 Specification
        Specification<T> specification = SpecificationBuilderUtils.build(criteriaList, orderByList, groupByList);

        // 執行查詢
        return repository.findAll(specification, pageable);
    }

    public <T> Page<T> dynamicSearchWithHaving(JpaSpecificationExecutor<T> repository,
                                            List<SearchCriteria<?>> criteriaList,
                                            List<SortCriteria> orderByList,
                                            List<String> groupByList,
                                            List<HavingCriteria<?>> havingList,
                                            Pageable pageable) {

        // 使用 SpecificationBuilder 構建 Specification
        Specification<T> specification = SpecificationBuilderUtils.build(criteriaList, orderByList, groupByList, havingList);

        // 執行查詢
        return repository.findAll(specification, pageable);
    }
}

