package com.mis7ake7411.tddprojectdemo.service.smsHistory.impl;

import com.mis7ake7411.tddprojectdemo.enums.DateTimeFormattersEnum;
import com.mis7ake7411.tddprojectdemo.model.bo.QuerySmsHistoryBO;
import com.mis7ake7411.tddprojectdemo.model.dto.PageResponseDTO;
import com.mis7ake7411.tddprojectdemo.model.dto.QuerySmsHistoryDataDTO;
import com.mis7ake7411.tddprojectdemo.model.vo.QuerySmsHistoryVO;
import com.mis7ake7411.tddprojectdemo.repository.SmsHistoryQueryRepository;
import com.mis7ake7411.tddprojectdemo.repository.SmsHistoryRepository;
import com.mis7ake7411.tddprojectdemo.service.agent.PersonPublicService;
import com.mis7ake7411.tddprojectdemo.service.smsHistory.SmsHistoryService;
import com.mis7ake7411.tddprojectdemo.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsHistoryServiceImpl implements SmsHistoryService {
    private final SmsHistoryRepository smsHistoryRepository;
    private final SmsHistoryQueryRepository smsHistoryQueryRepository;
    private final PersonPublicService personPublicService;

    @Transactional(readOnly = true)
    @Override
    public QuerySmsHistoryVO querySmsHistoryData(QuerySmsHistoryBO bo) {
        log.info("querySmsHistoryData : {}", bo);

        LocalDateTime sendDateStart = DateTimeUtils.parseToLocalDateTime(bo.getSendDateStart(), false, DateTimeFormattersEnum.DATE_WITH_DASH.getPattern());
        LocalDateTime sendDateEnd = DateTimeUtils.parseToLocalDateTime(bo.getSendDateEnd(), true, DateTimeFormattersEnum.DATE_WITH_DASH.getPattern());
        String phoneNumber = bo.getPhoneNumber();
        Set<String> agentIDList = bo.getAgentIDList() != null ? bo.getAgentIDList() : Collections.emptySet();
        Set<String> smsItemIDList = bo.getSmsItemIDList() != null ? bo.getSmsItemIDList() : Collections.emptySet();
        String isIvrSend = bo.getSmsSendType();
        boolean hasAgentId = !agentIDList.isEmpty();
        boolean hasSmsItemId = !smsItemIDList.isEmpty();

        int page = 0;
        int size = 1000;  // 每次查詢 1000 筆
        List<QuerySmsHistoryVO.SmsHistoryDTO> dtoList = new ArrayList<>();
        Page<QuerySmsHistoryDataDTO> pageSmsHistoryData;
        do {
            Pageable pageable = PageRequest.of(page, size);
            pageSmsHistoryData = smsHistoryRepository.querySmsHistoryData(agentIDList, sendDateStart, sendDateEnd, phoneNumber, smsItemIDList, isIvrSend, hasAgentId, hasSmsItemId, pageable);

            dtoList = convertToDTO(pageSmsHistoryData.getContent());
            page++;
        } while (pageSmsHistoryData.hasNext());

        QuerySmsHistoryVO querySmsHistoryVO = new QuerySmsHistoryVO();
        querySmsHistoryVO.setContent(dtoList);

        return querySmsHistoryVO;
    }

    @Transactional(readOnly = true)
    @Override
    public QuerySmsHistoryVO querySmsHistoryDataWithQueryDSL(QuerySmsHistoryBO bo) {
        int page = 0;
        int size = 1000;  // 每次查詢 1000 筆
        List<QuerySmsHistoryVO.SmsHistoryDTO> dtoList = new ArrayList<>();
        Page<QuerySmsHistoryDataDTO> pageSmsHistoryData;
        do {
            Pageable pageable = PageRequest.of(page, size);
            pageSmsHistoryData = smsHistoryQueryRepository.querySmsHistoryDataWithQueryDSL(bo, pageable);

            dtoList = convertToDTO(pageSmsHistoryData.getContent());
            page++;
        } while (pageSmsHistoryData.hasNext());

        QuerySmsHistoryVO querySmsHistoryVO = new QuerySmsHistoryVO();
        querySmsHistoryVO.setContent(dtoList);

        return querySmsHistoryVO;
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponseDTO<QuerySmsHistoryVO.SmsHistoryDTO> querySmsHistoryDataWithQueryDSL(QuerySmsHistoryBO bo, int pageNumber, int pageSize) {
        log.info("querySmsHistoryDataWithQueryDSL : {}", bo);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<QuerySmsHistoryDataDTO> pageSmsHistoryData = smsHistoryQueryRepository.querySmsHistoryDataWithQueryDSL(bo, pageable);

        List<QuerySmsHistoryVO.SmsHistoryDTO> dtoList = convertToDTO(pageSmsHistoryData.getContent());

        // 將查詢結果轉換為 Page 物件
        Page<QuerySmsHistoryVO.SmsHistoryDTO> pageResult = new PageImpl<>(dtoList, pageable, pageSmsHistoryData.getTotalElements());

        return new PageResponseDTO<>(pageResult);
    }

    private List<QuerySmsHistoryVO.SmsHistoryDTO> convertToDTO(List<QuerySmsHistoryDataDTO> dataList) {
        List<QuerySmsHistoryVO.SmsHistoryDTO> dtoList = new ArrayList<>();
        for (QuerySmsHistoryDataDTO data : dataList) {
            QuerySmsHistoryVO.SmsHistoryDTO dto = new QuerySmsHistoryVO.SmsHistoryDTO();
            dto.setSmsHistoryID(data.getSmsHistoryID());
            dto.setSendDate(data.getSendDate());
            dto.setPhoneNumber(data.getPhoneNumber());
            dto.setSmsItemName(joinItemNameWithArrow(data.getSmsCategoryName(), data.getSmsItemName()));
            Long agentID = StringUtils.isNotEmpty(data.getAgentID()) ? Long.valueOf(data.getAgentID()) : null;
            dto.setAgentName(agentID != null ? personPublicService.getPersonDNAndAccount(agentID) : "");
            dto.setIsIvrSend(data.getIsIvrSend());
            dto.setIvrCategory(data.getIvrCategory());
            dtoList.add(dto);
        }
        return dtoList;
    }

    private String joinItemNameWithArrow(String left, String right){
        if (StringUtils.isEmpty(left)) {
            return StringUtils.isEmpty(right) ? "" : right;
        }
        if (StringUtils.isEmpty(right)) {
            return left;
        }
        return left + " > " + right;
    }
}
