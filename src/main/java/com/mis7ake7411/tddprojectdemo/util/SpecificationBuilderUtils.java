package com.mis7ake7411.tddprojectdemo.util;

import com.mis7ake7411.tddprojectdemo.enums.SearchOperationEnum;
import com.mis7ake7411.tddprojectdemo.model.HavingCriteria;
import com.mis7ake7411.tddprojectdemo.model.SearchCriteria;
import com.mis7ake7411.tddprojectdemo.model.SortCriteria;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * JPA 動態查詢條件建構器
 * <p>
 * 此類用於建構 JPA Specification 查詢條件，支援多種查詢操作、排序和分組功能。
 * </p>
 */
public class SpecificationBuilderUtils {
    // 默認初始容量
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    /**
     * 建構基本查詢條件
     *
     * @param criteriaList 查詢條件列表
     * @param <T> 實體類型
     * @return 查詢規格
     */
    public static <T> Specification<T> build(List<SearchCriteria<?>> criteriaList) {
        return build(criteriaList != null ? criteriaList : Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    /**
     * 建構帶排序的查詢條件
     *
     * @param criteriaList 查詢條件列表
     * @param orderByList 排序條件列表
     * @param <T> 實體類型
     * @return 查詢規格
     */
    public static <T> Specification<T> build(List<SearchCriteria<?>> criteriaList,
        List<SortCriteria> orderByList) {

        return build(criteriaList != null ? criteriaList : new ArrayList<>(),
            orderByList != null ? orderByList : Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    /**
     * 建構帶排序和分組的查詢條件
     *
     * @param criteriaList 查詢條件列表
     * @param orderByList 排序條件列表
     * @param groupByList 分組條件列表
     * @param <T> 實體類型
     * @return 查詢規格
     */
    public static <T> Specification<T> build(List<SearchCriteria<?>> criteriaList,
        List<SortCriteria> orderByList, List<String> groupByList) {
        return build(criteriaList != null ? criteriaList : Collections.emptyList(),
            orderByList != null ? orderByList : Collections.emptyList(),
            groupByList != null ? groupByList : Collections.emptyList(), Collections.emptyList());
    }

    /**
     * 建構完整的查詢條件，包含查詢、排序和分組
     *
     * @param criteriaList 查詢條件列表
     * @param orderByList 排序條件列表
     * @param groupByList 分組條件列表
     * @param havingList having 子句條件列表
     * @param <T> 實體類型
     * @return 查詢規格
     */
    public static <T> Specification<T> build(List<SearchCriteria<?>> criteriaList,List<SortCriteria> orderByList,
        List<String> groupByList, List<HavingCriteria<?>> havingList) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>(
                    criteriaList != null ? criteriaList.size() : DEFAULT_INITIAL_CAPACITY);

            // 處理查詢條件
            if (!CollectionUtils.isEmpty(criteriaList)) {
                processCriteria(criteriaList, root, criteriaBuilder, predicates);
            }

            if (query != null) {
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
                    List<Order> orderExpressions  = new ArrayList<>();
                    for (SortCriteria sortCriteria : orderByList) {
                        if (sortCriteria.isAscending()) {
                            orderExpressions .add(criteriaBuilder.asc(root.get(sortCriteria.getKey())));
                        } else {
                            orderExpressions .add(criteriaBuilder.desc(root.get(sortCriteria.getKey())));
                        }
                    }
                    query.orderBy(orderExpressions);
                }

                // having 條件
                if (havingList != null && !havingList.isEmpty()) {
                    List<Predicate> havingPredicates = buildHavingPredicates(havingList, root, criteriaBuilder);
                    query.having(havingPredicates.toArray(new Predicate[0]));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 處理基本查詢條件
     */
    private static <T> void processCriteria(
            List<SearchCriteria<?>> criteriaList,
            Root<T> root,
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates) {

        for (SearchCriteria<?> criteria : criteriaList) {
            if (!isValidCriteria(criteria)) {
                continue;
            }
            try {
                predicates.add(buildPredicate(criteria, root, criteriaBuilder));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid search criteria: " + criteria.getKey()
                        + " : " + criteria.getOperation(), e);
            }
        }
    }

    /**
     * 檢查查詢條件是否有效
     */
    private static boolean isValidCriteria(SearchCriteria<?> criteria) {
        if (criteria.getKey() == null) {
            return false;
        }
        // 當查詢條件 是 IS_NULL 或 IS_NOT_NULL 時，不需要檢查 value
        return criteria.getOperation() == SearchOperationEnum.IS_NULL
                || criteria.getOperation() == SearchOperationEnum.IS_NOT_NULL
                || criteria.getValue() != null;
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
    private static <T> Predicate buildPredicate(SearchCriteria<?> criteria, Root<T> root,
        CriteriaBuilder criteriaBuilder) {

        // 處理常規查詢
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
                } else {
                    throw new IllegalArgumentException("IN 操作需要傳入集合類型的值");
                }
            case NOT_IN:
                if (criteria.getValue() instanceof Collection) {
                    return criteriaBuilder.not(root.get(criteria.getKey()).in((Collection<?>) criteria.getValue()));
                } else {
                    throw new IllegalArgumentException("NOT IN 操作需要傳入集合類型的值");
                }
            case GREATER_THAN:
                if (criteria.getValue() instanceof Comparable) {
                    return criteriaBuilder.greaterThan(root.get(criteria.getKey()), (Comparable) criteria.getValue());
                } else {
                    throw new IllegalArgumentException("GREATER_THAN_OR_EQUAL 操作需要 Comparable 類型的值");
                }
            case GREATER_THAN_OR_EQUAL:
                if (criteria.getValue() instanceof Comparable) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), (Comparable) criteria.getValue());
                } else {
                    throw new IllegalArgumentException("LESS_THAN 操作需要 Comparable 類型的值");
                }
            case LESS_THAN:
                if (criteria.getValue() instanceof Comparable) {
                    return criteriaBuilder.lessThan(root.get(criteria.getKey()), (Comparable) criteria.getValue());
                }
            case LESS_THAN_OR_EQUAL:
                if (criteria.getValue() instanceof Comparable) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), (Comparable) criteria.getValue());
                } else {
                    throw new IllegalArgumentException("LESS_THAN_OR_EQUAL 操作需要 Comparable 類型的值");
                }
            case BETWEEN:
                if (criteria.getValue() instanceof List<?> values) {
                    if (values.size() == 2 && values.get(0) instanceof Comparable && values.get(1) instanceof Comparable) {
                        Path<Comparable> path = root.get(criteria.getKey());
                        return criteriaBuilder.between(path, (Comparable) values.get(0), (Comparable) values.get(1));
                    } else {
                        throw new IllegalArgumentException("BETWEEN 操作需要包含兩個 Comparable 值的列表");
                    }
                } else {
                    throw new IllegalArgumentException("BETWEEN 操作需要傳入列表");
                }
            case IS_NULL:
                return criteriaBuilder.isNull(root.get(criteria.getKey()));
            case IS_NOT_NULL:
                return criteriaBuilder.isNotNull(root.get(criteria.getKey()));
            default:
                throw new IllegalArgumentException("查詢條件不支援: " + criteria.getOperation());
        }
    }

    /**
     * 構建 HAVING 條件
     *
     * @param havingList having 條件列表
     * @param root JPA Root 物件
     * @param criteriaBuilder JPA CriteriaBuilder 物件
     * @param <T> 實體類型
     * @return HAVING 條件 Predicate 列表
     */
    private static <T> List<Predicate> buildHavingPredicates(List<HavingCriteria<?>> havingList, Root<T> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> havingPredicates = new ArrayList<>();

        for (HavingCriteria<?> having : havingList) {
            Expression<? extends Number> aggregateExpression;
            Comparable<Object> comparableValue;
            // 確認 value 是否為 Comparable
            if (!(having.getValue() instanceof Comparable)) {
                throw new IllegalArgumentException("HAVING 條件的值必須是 Comparable 類型");
            } else {
                comparableValue = (Comparable<Object>) having.getValue();
            }

            // 生成聚合函數的 Expression
            switch (having.getAggregationFunction().toUpperCase()) {
                case "SUM":
                    aggregateExpression = criteriaBuilder.sum(root.get(having.getKey())).as(Number.class);
                    break;
                case "COUNT":
                    aggregateExpression = criteriaBuilder.count(root.get(having.getKey())).as(Number.class);
                    break;
                case "AVG":
                    aggregateExpression = criteriaBuilder.avg(root.get(having.getKey())).as(Number.class);
                    break;
                case "MAX":
                    aggregateExpression = criteriaBuilder.max(root.get(having.getKey())).as(Number.class);
                    break;
                case "MIN":
                    aggregateExpression = criteriaBuilder.min(root.get(having.getKey())).as(Number.class);
                    break;
                default:
                    throw new IllegalArgumentException("不支援的聚合函數: " + having.getAggregationFunction());
            }

            // 構建 Predicate，基於比較運算子
            switch (having.getOperation()) {
                case EQUAL:
                    havingPredicates.add(criteriaBuilder.equal(aggregateExpression, comparableValue));
                    break;
                case GREATER_THAN:
                    havingPredicates.add(criteriaBuilder.greaterThan((Expression<Comparable>) aggregateExpression, comparableValue));
                    break;
                case GREATER_THAN_OR_EQUAL:
                    havingPredicates.add(criteriaBuilder.greaterThanOrEqualTo((Expression<Comparable>) aggregateExpression, comparableValue));
                    break;
                case LESS_THAN:
                    havingPredicates.add(criteriaBuilder.lessThan((Expression<Comparable>) aggregateExpression, comparableValue));
                    break;
                case LESS_THAN_OR_EQUAL:
                    havingPredicates.add(criteriaBuilder.lessThanOrEqualTo((Expression<Comparable>) aggregateExpression, comparableValue));
                    break;
                default:
                    throw new IllegalArgumentException("HAVING 比較操作不支援: " + having.getOperation());
            }
        }

        return havingPredicates;
    }

}

