package com.mis7ake7411.tddprojectdemo.util;

import com.mis7ake7411.tddprojectdemo.enums.SearchOperationEnum;
import com.mis7ake7411.tddprojectdemo.model.base.SearchCriteria;
import com.mis7ake7411.tddprojectdemo.model.base.SortCriteria;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 * JPA 動態查詢條件建構器
 * <p>
 * 此類用於建構 JPA Specification 查詢條件，支援多種查詢操作、排序和分組功能。
 * </p>
 */
public class SpecificationBuilderUtils<T> {

    /**
     * 建構基本查詢條件
     *
     * @param criteriaList 查詢條件列表
     * @param <T> 實體類型
     * @return 查詢規格
     */
    public static <T> Specification<T> build(List<SearchCriteria<?>> criteriaList) {
        return build(criteriaList != null ? criteriaList : new ArrayList<>(),
            null, null);
    }

    /**
     * 建構帶排序的查詢條件
     *
     * @param criteriaList 查詢條件列表
     * @param orderByList 排序條件列表
     * @param <T> 實體類型
     * @return 查詢規格
     */
    public static <T> Specification<T> build(List<SearchCriteria<?>> criteriaList, List<SortCriteria> orderByList) {
        return build(criteriaList != null ? criteriaList : new ArrayList<>(),
            orderByList != null ? orderByList : new ArrayList<>(), null);
    }

    /**
     * 建構完整的查詢條件，包含查詢、排序和分組
     *
     * @param criteriaList 查詢條件列表
     * @param orderByList 排序條件列表
     * @param groupByList 分組條件列表
     * @param <T> 實體類型
     * @return 查詢規格
     */
    public static <T> Specification<T> build(List<SearchCriteria<?>> criteriaList,List<SortCriteria> orderByList, List<String> groupByList) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (SearchCriteria<?> criteria : criteriaList) {
                if (criteria.getKey() == null) {
                    continue;
                }
                // 當查詢條件 是 IS_NULL 或 IS_NOT_NULL 時，不需要檢查 value
                if (criteria.getOperation() != SearchOperationEnum.IS_NULL
                    && criteria.getOperation() != SearchOperationEnum.IS_NOT_NULL
                    && criteria.getValue() == null) {
                    continue;
                }
                try {
                    predicates.add(buildPredicate(criteria, root, criteriaBuilder));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("查詢條件不支援: " + criteria.getKey() + " : " + criteria.getOperation());
                }
            }

            // where 條件
            query.where(predicates.toArray(new Predicate[0]));

            // group by 條件
            if (groupByList != null && !groupByList.isEmpty()) {
                List<Expression<?>> groupByExpressions = new ArrayList<>();
                for (String field : groupByList) {
                    groupByExpressions.add(root.get(field));
                }
                query.groupBy(groupByExpressions);
            }

            // order by 條件
            if (orderByList != null && !orderByList.isEmpty()) {
                List<Order> orders = new ArrayList<>();
                for (SortCriteria sortCriteria : orderByList) {
                    if (sortCriteria.isAscending()) {
                        orders.add(criteriaBuilder.asc(root.get(sortCriteria.getKey())));
                    } else {
                        orders.add(criteriaBuilder.desc(root.get(sortCriteria.getKey())));
                    }
                }
                query.orderBy(orders);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 建構查詢條件
     *
     * @param criteria 查詢條件
     * @param root JPA Root 物件
     * @param criteriaBuilder JPA CriteriaBuilder 物件
     * @param <T> 實體類型
     * @return 查詢條件 Predicate
     * @throws IllegalArgumentException 當查詢操作不支援時拋出
     */
    private static <T> Predicate buildPredicate(SearchCriteria<?> criteria, Root<T> root, CriteriaBuilder criteriaBuilder) {
        switch (criteria.getOperation()) {
            case EQUAL:
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NOT_EQUAL:
                return criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case LIKE:
                return criteriaBuilder.like(root.get(criteria.getKey()),
                    "%" + criteria.getValue() + "%");
            case NOT_LIKE:
                return criteriaBuilder.notLike(root.get(criteria.getKey()),
                    "%" + criteria.getValue() + "%");
            case IN:
                if (criteria.getValue() instanceof Collection) {
                    return root.get(criteria.getKey()).in((Collection<?>) criteria.getValue());
                }
            case NOT_IN:
                if (criteria.getValue() instanceof Collection) {
                    return criteriaBuilder.not(root.get(criteria.getKey()).in((Collection<?>) criteria.getValue()));
                }
            case GREATER_THAN:
                if (criteria.getValue() instanceof Comparable) {
                    return criteriaBuilder.greaterThan(root.get(criteria.getKey()), (Comparable) criteria.getValue());
                }
                break;
            case GREATER_THAN_OR_EQUAL:
                if (criteria.getValue() instanceof Comparable) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), (Comparable) criteria.getValue());
                }
                break;
            case LESS_THAN:
                if (criteria.getValue() instanceof Comparable) {
                    return criteriaBuilder.lessThan(root.get(criteria.getKey()), (Comparable) criteria.getValue());
                }
                break;
            case LESS_THAN_OR_EQUAL:
                if (criteria.getValue() instanceof Comparable) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), (Comparable) criteria.getValue());
                }
                break;
            case BETWEEN:
                if (criteria.getValue() instanceof List) {
                    List<?> values = (List<?>) criteria.getValue();
                    if (values.size() == 2 && values.get(0) instanceof Comparable && values.get(1) instanceof Comparable) {
                        Path<Comparable> path = root.get(criteria.getKey());
                        return criteriaBuilder.between(path, 
                            (Comparable) values.get(0), 
                            (Comparable) values.get(1));
                    }
                }
            case IS_NULL:
                return criteriaBuilder.isNull(root.get(criteria.getKey()));
            case IS_NOT_NULL:
                return criteriaBuilder.isNotNull(root.get(criteria.getKey()));
            default:
                throw new IllegalArgumentException("查詢條件不支援: " + criteria.getOperation());
        }
        return null;
    }
}

