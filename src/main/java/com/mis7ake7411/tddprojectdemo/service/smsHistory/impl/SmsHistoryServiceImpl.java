package com.mis7ake7411.tddprojectdemo.service.smsHistory.impl;

import com.mis7ake7411.tddprojectdemo.model.bo.QuerySmsHistoryBO;
import com.mis7ake7411.tddprojectdemo.model.dto.QuerySmsHistoryDataDTO;
import com.mis7ake7411.tddprojectdemo.model.vo.QuerySmsHistoryVO;
import com.mis7ake7411.tddprojectdemo.repository.SmsHistoryRepository;
import com.mis7ake7411.tddprojectdemo.service.smsHistory.SmsHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsHistoryServiceImpl implements SmsHistoryService {
    private final SmsHistoryRepository smsHistoryRepository;

    @Transactional(readOnly = true)
    @Override
    public QuerySmsHistoryVO querySmsHistoryData(QuerySmsHistoryBO bo) {
        log.info("querySmsHistoryData : {}", bo);

        LocalDateTime sendDateStart = parseDate(bo.getSendDateStart(), false);
        LocalDateTime sendDateEnd = parseDate(bo.getSendDateEnd(), true);
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

            for (QuerySmsHistoryDataDTO dto : pageSmsHistoryData.getContent()) {
                QuerySmsHistoryVO.SmsHistoryDTO vo = new QuerySmsHistoryVO.SmsHistoryDTO();
                vo.setSmsHistoryID(dto.getSmsHistoryID());
                vo.setSendDate(dto.getSendDate());
                vo.setPhoneNumber(dto.getPhoneNumber());
                vo.setSmsItemName(joinItemNameWithArrow(dto.getSmsCategoryName(), dto.getSmsItemName()));
                Long agentID = StringUtils.isNotEmpty(dto.getAgentID()) ? Long.valueOf(dto.getAgentID()) : null;
//                vo.setAgentName(agentID != null ? personPublicService.getPersonDNAndAccount(agentID) : "");
                vo.setIsIvrSend(dto.getIsIvrSend());
                vo.setIvrCategory(dto.getIvrCategory());
                dtoList.add(vo);
            }
            page++;
        } while (pageSmsHistoryData.hasNext());

        QuerySmsHistoryVO querySmsHistoryVO = new QuerySmsHistoryVO();
        querySmsHistoryVO.setContent(dtoList);

        return querySmsHistoryVO;
    }

    private LocalDateTime parseDate(String date, boolean isEndOfDay) {
        if (StringUtils.isNotEmpty(date)) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return isEndOfDay ? localDate.atTime(LocalTime.MAX) : localDate.atStartOfDay();
        }
        return null;
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
