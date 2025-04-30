package com.mis7ake7411.tddprojectdemo.service.smsHistory;

import com.mis7ake7411.tddprojectdemo.model.bo.QuerySmsHistoryBO;
import com.mis7ake7411.tddprojectdemo.model.vo.QuerySmsHistoryVO;
import org.springframework.transaction.annotation.Transactional;

public interface SmsHistoryService {

    QuerySmsHistoryVO querySmsHistoryData(QuerySmsHistoryBO bo);
}
