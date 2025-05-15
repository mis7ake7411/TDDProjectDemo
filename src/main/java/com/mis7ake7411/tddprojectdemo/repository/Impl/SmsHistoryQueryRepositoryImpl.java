package com.mis7ake7411.tddprojectdemo.repository.Impl;

import com.mis7ake7411.tddprojectdemo.entity.*;
import com.mis7ake7411.tddprojectdemo.enums.DateTimeFormattersEnum;
import com.mis7ake7411.tddprojectdemo.model.bo.QuerySmsHistoryBO;
import com.mis7ake7411.tddprojectdemo.model.dto.QuerySmsHistoryDataDTO;
import com.mis7ake7411.tddprojectdemo.repository.SmsHistoryQueryRepository;
import com.mis7ake7411.tddprojectdemo.util.DateTimeUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class SmsHistoryQueryRepositoryImpl implements SmsHistoryQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<QuerySmsHistoryDataDTO> querySmsHistoryDataWithQueryDSL(
            QuerySmsHistoryBO bo,
            Pageable pageable
    ) {
        QSmsHistory history = QSmsHistory.smsHistory;
        QSMSTemplateItem item = QSMSTemplateItem.sMSTemplateItem;
        QSMSTemplateCategory category = QSMSTemplateCategory.sMSTemplateCategory;
        QSMSTemplateCategoryItem categoryItem = QSMSTemplateCategoryItem.sMSTemplateCategoryItem;
        QCfgPerson person = QCfgPerson.cfgPerson;
        // 查詢條件
        BooleanBuilder dynamicBuilder = dynamicQuery(bo, history, categoryItem );

        // 排序條件
        List<OrderSpecifier<?>> orderBy = new ArrayList<>();
        orderBy.add(history.dbid.desc());

        // 主查詢
        List<QuerySmsHistoryDataDTO> content = queryFactory
                .select(Projections.constructor(QuerySmsHistoryDataDTO.class,
                        history.dbid,
                        history.sendDateTime,
                        history.phoneNumber,
                        category.Name,
                        item.Subject,
                        history.agentID,
                        history.isIvrSend,
                        history.ivrCategory,
                        person.USER_NAME
                ))
                .from(history)
                .leftJoin(categoryItem).on(history.smsItemID.eq(categoryItem.itemID))
                .leftJoin(item).on(item.DBID.stringValue().eq(categoryItem.itemID))
                .leftJoin(category).on(category.DBID.stringValue().eq(categoryItem.CategoryID))
                .leftJoin(person).on(person.DBID.stringValue().eq(history.agentID))
                .where(dynamicBuilder)
                .orderBy(orderBy.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 總筆數查詢
        long total = getTotal(history, categoryItem, dynamicBuilder);

        // 回傳分頁資料
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanBuilder dynamicQuery(
            QuerySmsHistoryBO bo,
            QSmsHistory smsHistory,
            QSMSTemplateCategoryItem smsTemplateCategoryItem
    ) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (CollectionUtils.isNotEmpty(bo.getAgentIDList())) {
            booleanBuilder.and(smsHistory.agentID.in(bo.getAgentIDList()));
        }
        LocalDateTime start = DateTimeUtils.parseToLocalDateTime(bo.getSendDateStart(), false, DateTimeFormattersEnum.DATE_WITH_DASH.getPattern());
        if (start != null) {
            booleanBuilder.and(smsHistory.sendDateTime.goe(start));
        }
        LocalDateTime end = DateTimeUtils.parseToLocalDateTime(bo.getSendDateEnd(), true, DateTimeFormattersEnum.DATE_WITH_DASH.getPattern());
        if (end != null) {
            booleanBuilder.and(smsHistory.sendDateTime.loe(end));
        }
        if (StringUtils.isNotBlank(bo.getPhoneNumber())) {
            booleanBuilder.and(smsHistory.phoneNumber.contains(bo.getPhoneNumber()));
        }
        if (CollectionUtils.isNotEmpty(bo.getSmsItemIDList())) {
            booleanBuilder.and(smsTemplateCategoryItem.itemID.in(bo.getSmsItemIDList()));
        }
        if (StringUtils.isNotBlank(bo.getSmsSendType())) {
            booleanBuilder.and(smsHistory.isIvrSend.eq(bo.getSmsSendType()));
        }
        return booleanBuilder;
    }

    public Long getTotal(
            QSmsHistory smsHistory,
            QSMSTemplateCategoryItem smsTemplateCategoryItem,
            BooleanBuilder builder
    ) {
        return queryFactory
                .select(smsHistory.count())
                .from(smsHistory)
                .leftJoin(smsTemplateCategoryItem)
                .on(smsTemplateCategoryItem.itemID.eq(smsHistory.smsItemID))
                .where(builder)
                .fetchOne();
    }
}
