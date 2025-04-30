package com.mis7ake7411.tddprojectdemo.repository;

import com.mis7ake7411.tddprojectdemo.entity.SmsHistory;
import com.mis7ake7411.tddprojectdemo.model.dto.ExportSmsCountDataDTO;
import com.mis7ake7411.tddprojectdemo.model.dto.QuerySmsHistoryDataDTO;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface SmsHistoryRepository extends JpaRepository<SmsHistory, Long> {
    @Query("""
                    SELECT new com.mis7ake7411.tddprojectdemo.model.dto.ExportSmsCountDataDTO(
                        tsc.Name, tsh.isIvrSend, tsh.ivrCategory, tsti.Subject, COUNT(*)
                    )
                    FROM SmsHistory  tsh
                    LEFT JOIN SMSTemplateCategoryItem tsci
                        ON tsh.smsItemID = tsci.itemID
                    LEFT JOIN SMSTemplateItem tsti
                        ON tsti.DBID = Cast(tsci.itemID AS Long)
                    LEFT JOIN SMSTemplateCategory tsc
                        ON tsc.DBID =  Cast(tsci.CategoryID AS Long)
                    WHERE (:sendDateStart IS NULL OR tsh.sendDateTime >= :sendDateStart)
                        AND (:sendDateEnd IS NULL OR tsh.sendDateTime <= :sendDateEnd)
                        AND (:phoneNumber IS NULL OR tsh.phoneNumber LIKE CONCAT(:phoneNumber, '%'))
                        AND (:isIvrSend IS NULL OR tsh.isIvrSend = :isIvrSend)
                        AND (:hasAgentId = false  OR tsh.agentID IN :agentIds)
                        AND (:hasSmsItemId = false  OR tsh.smsItemID IN :smsItemIds)
                    GROUP BY
                        tsc.Name, tsh.isIvrSend, tsh.ivrCategory, tsti.Subject
            """)
    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly", value = "true"),
            @QueryHint(name = "org.hibernate.fetchSize", value = "1000"),   // 用來設定每次從資料庫讀取的記錄數進到緩衝區大小
            @QueryHint(name = "org.hibernate.timeout", value = "60000")   // 60 seconds
    })
    List<ExportSmsCountDataDTO> exportSmsCountData(
            @Param("agentIds") Collection<String> agentIds,
            @Param("sendDateStart") LocalDateTime sendDateStart,
            @Param("sendDateEnd") LocalDateTime sendDateEnd,
            @Param("phoneNumber") String phoneNumber,
            @Param("smsItemIds") Collection<String> smsItemIds,
            @Param("isIvrSend") String isIvrSend,
            @Param("hasAgentId") boolean hasAgentId,
            @Param("hasSmsItemId") boolean hasSmsItemId
    );

    @Query("""
                    SELECT new com.mis7ake7411.tddprojectdemo.model.dto.QuerySmsHistoryDataDTO(
                        tsh.dbid,
                        tsh.sendDateTime, 
                        tsh.phoneNumber,
                        tsc.Name, 
                        tsti.Subject,
                        tsh.agentID, 
                        tsh.isIvrSend, 
                        tsh.ivrCategory
                    )
                    FROM SmsHistory  tsh
                    LEFT JOIN SMSTemplateCategoryItem tsci
                        ON tsh.smsItemID = tsci.itemID
                    LEFT JOIN SMSTemplateItem tsti
                        ON tsti.DBID = Cast(tsci.itemID AS Long)
                    LEFT JOIN SMSTemplateCategory tsc
                        ON tsc.DBID =  Cast(tsci.CategoryID AS Long)
                    WHERE (:sendDateStart IS NULL OR tsh.sendDateTime >= :sendDateStart)
                        AND (:sendDateEnd IS NULL OR tsh.sendDateTime <= :sendDateEnd)
                        AND (:phoneNumber IS NULL OR tsh.phoneNumber LIKE CONCAT(:phoneNumber, '%'))
                        AND (:isIvrSend IS NULL OR tsh.isIvrSend = :isIvrSend)
                        AND (:hasAgentId = false OR tsh.agentID IN :agentIds)
                        AND (:hasSmsItemId = false OR tsh.smsItemID IN :smsItemIds)
                """)
    Page<QuerySmsHistoryDataDTO> querySmsHistoryData(
            @Param("agentIds") Collection<String> agentIds,
            @Param("sendDateStart") LocalDateTime sendDateStart,
            @Param("sendDateEnd") LocalDateTime sendDateEnd,
            @Param("phoneNumber") String phoneNumber,
            @Param("smsItemIds") Collection<String> smsItemIds,
            @Param("isIvrSend") String isIvrSend,
            @Param("hasAgentId") boolean hasAgentId,
            @Param("hasSmsItemId") boolean hasSmsItemId,
            Pageable pageable
    );
}
