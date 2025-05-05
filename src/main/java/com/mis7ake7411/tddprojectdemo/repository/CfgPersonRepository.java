package com.mis7ake7411.tddprojectdemo.repository;

import com.mis7ake7411.tddprojectdemo.entity.CfgPerson;
import com.mis7ake7411.tddprojectdemo.model.dto.PilotNameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * tblCfg_Person 儲存庫
 */
public interface CfgPersonRepository extends JpaRepository<CfgPerson, Long> {

    /**
     * 依據 DN、STATE 查詢 tblCfg_Person 的 DBID 數量
     *
     * @param aDN    分機
     * @param aState 人員帳號狀態 0:正常啟用中 1: 已停用 2: 已鎖定 3: 已刪除
     * @return count
     */
    @Query(value = " select count(DBID) from tblCfg_Person with(nolock) where DN = :DN AND STATE <> :State ", nativeQuery = true)
    Integer getCountByDNAndStateNot(
            @Param("DN") String aDN,
            @Param("State") String aState
    );

    /**
     * 依據 DN、STATE、ACCOUNT 查詢 tblCfg_Person 的 DBID 數量
     *
     * @param aDN      分機
     * @param aState   人員帳號狀態 0:正常啟用中 1: 已停用 2: 已鎖定 3: 已刪除
     * @param aAccount 帳號
     * @return count
     */
    @Query(value = " select count(DBID) from tblCfg_Person with(nolock) where DN = :DN AND STATE <> :State AND ACCOUNT <> :Account ", nativeQuery = true)
    Integer getCountByDNAndStateNotAndAccountNot(
            @Param("DN") String aDN,
            @Param("State") String aState,
            @Param("Account") String aAccount
    );

    /**
     * 依據 ACCOUNT 查詢 tblCfg_Person 第1筆資訊
     *
     * @param aAccount 帳號
     * @return tblCfg_Person 資訊
     */
    @Query(value = "SELECT TOP 1 * FROM tblCfg_Person with(nolock) WHERE ACCOUNT = :Account "
            , nativeQuery = true)
    CfgPerson PersonByAccount(
            @Param("Account") String aAccount
    );

    /**
     * Task 27509: [王道銀TM] 登入頁：帳號登入時不區分大小寫
     *
     * @param aAccount 帳號
     * @return tblCfg_Person 資訊
     */
    @Query(value = "SELECT TOP 1 * FROM tblCfg_Person with(nolock) WHERE UPPER(ACCOUNT) = :Account"
            , nativeQuery = true)
    CfgPerson PersonByAccountCaseInsensitive(
            @Param("Account") String aAccount
    );

    /**
     * 依據 TenantID、STATE 查詢 tblCfg_Person 資訊 (排序: ORDER BY: DN(分機), ACCOUNT)
     *
     * @param aTenantID 公司別
     * @param aSTATE    人員帳號狀態 0:正常啟用中 1: 已停用 2: 已鎖定 3: 已刪除
     * @return tblCfg_Person 資訊
     */
    @Query(value = "SELECT * FROM [dbo].[tblCfg_Person] with(nolock) where STATE=:STATE AND TenantID=:TenantID ORDER BY TRY_CAST(COALESCE(NULLIF(LTRIM(RTRIM(DN)), ''), '0') AS BIGINT), ACCOUNT"
            , nativeQuery = true)
    List<CfgPerson> findPersonByStateOrderByDnAndAccount(
            @Param("TenantID") String aTenantID,
            @Param("STATE") String aSTATE);

    /**
     * 依據 TenantID、STATE 查詢 tblCfg_Person 資訊 (排序: ORDER BY: DN(分機), ACCOUNT)
     *
     * @param aTenantID 公司別
     * @param aSTATEs   人員帳號狀態 0:正常啟用中 1: 已停用 2: 已鎖定 3: 已刪除
     * @return tblCfg_Person 資訊
     */
    @Query(value = "SELECT * FROM [dbo].[tblCfg_Person] with(nolock) where STATE IN :STATES AND TenantID=:TenantID ORDER BY TRY_CAST(COALESCE(NULLIF(LTRIM(RTRIM(DN)), ''), '0') AS BIGINT), ACCOUNT"
            , nativeQuery = true)
    List<CfgPerson> findPersonByStatesOrderByDnAndAccount(
            @Param("TenantID") String aTenantID,
            @Param("STATES") Set<String> aSTATEs);

    /**
     * 依據 TenantID 查詢 tblCfg_Person 資訊 (排序: ORDER BY: DN(分機), ACCOUNT)
     *
     * @param aTenantID 公司別
     * @return tblCfg_Person 資訊
     */
    @Query(value = "SELECT * FROM [dbo].[tblCfg_Person] with(nolock) where TenantID=:TenantID ORDER BY TRY_CAST(COALESCE(NULLIF(LTRIM(RTRIM(DN)), ''), '0') AS BIGINT), ACCOUNT"
            , nativeQuery = true)
    List<CfgPerson> findPersonAllOrderByDnAndAccount(
            @Param("TenantID") String aTenantID);

    /**
     * 依據 DBID 查詢 tblCfg_Person 資訊
     *
     * @param dbIds 流水號
     * @return tblCfg_Person 資訊
     */
    @Query(value = "SELECT * FROM [dbo].[tblCfg_Person] with(nolock) WHERE (:#{#dbIds.size()} = 0 OR DBID IN :dbIds)", nativeQuery = true)
    List<CfgPerson> findPersonByDbIds(
            @Param("dbIds") Set<String> dbIds
    );

    /**
     * 依據 DBID、STATE = '0' 查詢 tblCfg_Person 資訊
     *
     * @param dbIds 流水號
     * @return tblCfg_Person 資訊
     */
    @Query(value = "SELECT * FROM [dbo].[tblCfg_Person] with(nolock) WHERE STATE = '0' AND (:#{#dbIds.size()} = 0 OR DBID IN :dbIds)", nativeQuery = true)
    List<CfgPerson> findALivePersonByDbIds(
            @Param("dbIds") Set<String> dbIds
    );

    /**
     * 依據 DN 查詢 tblCfg_Person 資訊
     *
     * @param DNs 分機
     * @return tblCfg_Person 資訊
     */
    @Query(value = "SELECT * FROM tblCfg_Person with(nolock) where DN IN :DNs"
            , nativeQuery = true)
    List<CfgPerson> findByDNs(
            @Param("DNs") Set<String> DNs
    );

    /**
     * 依據 DBID 查詢 tblCfg_Person 的 SwitchAccount 欄位資訊
     *
     * @param dbId 流水號
     * @return EMPLOYEE_ID 欄位資訊
     */
    @Query(value = "SELECT SwitchAccount FROM tblCfg_Person with(nolock) WHERE DBID = :dbId"
            , nativeQuery = true)
    String findEmployeeIdByDbId(
            @Param("dbId") String dbId
    );

    /**
     * 依據 switchAccount 查詢 tblCfg_Person 的 DBID 欄位資訊
     *
     * @param switchAccount 員工編號
     * @return DBID
     */
    @Query(value = "SELECT TOP(1) DBID FROM tblCfg_Person with(nolock) WHERE SwitchAccount = :switchAccount AND STATE = '0'"
            , nativeQuery = true)
    String findDbIdBySwitchAccount(
            @Param("switchAccount") String switchAccount
    );

    /**
     * 依據 DN 查詢 tblCfg_Person 第1筆資訊
     *
     * @param dn 分機
     * @return tblCfg_Person 資訊
     */
    @Query(value = "SELECT TOP(1) * FROM tblCfg_Person with(nolock) WHERE DN = :dn AND STATE = '0'"
            , nativeQuery = true)
    CfgPerson findDbIdByDn(
            @Param("dn") String dn
    );

    /**
     * 依據 DBID 查詢 tblCfg_Person 資訊
     *
     * @param dbId 流水號
     * @return tblCfg_Person 資訊
     */
    @Query(value = "SELECT * FROM tblCfg_Person with(nolock) WHERE DBID = :dbId"
            , nativeQuery = true)
    CfgPerson findByDbId(
            @Param("dbId") String dbId
    );

    /**
     * 依據 DBID 查詢 tblCfg_Person 資訊
     *
     * @param DBIDs 流水號
     * @return tblCfg_Person 資訊
     */
    @Query(value = "SELECT * FROM tblCfg_Person with(nolock) where DBID IN :DBIDs"
            , nativeQuery = true)
    List<CfgPerson> findByDBIDs(
            @Param("DBIDs") Set<Long> DBIDs
    );

    @Query(value = "SELECT * FROM tblCfg_Person with(nolock)"
            , nativeQuery = true)
    List<CfgPerson> findAllPerson();

    /**
     * 依據 ACCOUNT 查詢 tblCfg_Person 第1筆資訊
     *
     * @param ACCOUNT 帳號
     * @return tblCfg_Person 資訊
     */
    @Query(
            value = "SELECT * " +
                    "FROM tblCfg_Person WITH(nolock) " +
                    "WHERE ACCOUNT IN :ACCOUNT " +
                    "AND TenantID =:TenantID"
            , nativeQuery = true
    )
    List<CfgPerson> getPersonListByAccount(
            @Param("ACCOUNT") Set<String> ACCOUNT,
            @Param("TenantID") Long TenantID
    );

    /**
     * 依據 employeeId 查詢 tblCfg_Person 的 DBID 欄位資訊
     *
     * @param employeeID 員工編號
     * @return DBID
     */
    @Query(
            value = "SELECT * " +
                    "FROM tblCfg_Person WITH(nolock) " +
                    "WHERE EMPLOYEE_ID IN :employeeID " +
                    "AND TenantID =:TenantID"
            , nativeQuery = true
    )
    List<CfgPerson> getPersonListByEmployeeId(
            @Param("employeeID") Set<String> employeeID,
            @Param("TenantID") Long TenantID
    );

    @Query(value = "SELECT COUNT(DBID) AS [Count] FROM tblCfg_Person with(nolock) WHERE DN = :dn AND DBID <> :DBID "
            , nativeQuery = true)
    int findCountByDnAndNotAgentID(
            @Param("dn") String dn,
            @Param("DBID") Long DBID
    );

    @Transactional
    @Modifying
    @Query(
            value = "UPDATE tblCfg_Person SET " +
                    " DN = N'', " +
                    " DNPwd = N'', " +
                    " EMPLOYEE_ID = N'', " +
                    " SwitchAccount = N'', " +
                    " SwitchPwd = N'' " +
                    " WHERE TenantID = :tenantID ",
            nativeQuery = true
    )
    int clearDnAndDnPwdAndEmployeeID(@Param("tenantID") Long tenantID);

    @Query(
            value = "SELECT ACCOUNT " +
                    "FROM tblCfg_Person WITH(nolock) " +
                    "WHERE TenantID =:tenantID"
            , nativeQuery = true
    )
    List<String> getAccountList(@Param("tenantID") Long tenantID);

    @Query(value = "SELECT EmailSignature FROM tblCfg_Person WITH(nolock) WHERE DBID =:dbid "
            , nativeQuery = true)
    String findEmailSignature(@Param("dbid") String dbid);

    @Transactional
    @Modifying
    @Query(value = "UPDATE tblCfg_Person " +
            "SET EmailSignature = :emailSignature " +
            "WHERE TenantID =:tenantID " +
            "AND DBID=:dbid"
            , nativeQuery = true
    )
    void updateEmailSignatureByPerson(@Param("dbid") Long dbid, @Param("tenantID") Long tenantID, @Param("emailSignature") String emailSignature);

    @Query(value = "SELECT USER_NAME, DBID " +
            "FROM tblCfg_Person WITH (NOLOCK) " +
            "WHERE tblCfg_Person.STATE = '0' OR tblCfg_Person.STATE = '3' " +
            "AND (:pilotId IS NULL OR LEN(:pilotId) = 0 OR tblCfg_Person.DBID IN " +
            "(SELECT AgentID FROM tblCfg_Pilot_Agent WHERE TenantID = :tenantId " +
            "AND Pilot IN (SELECT Pilot FROM tblCfg_Pilot WHERE Enable = :enable " +
            "AND TenantID = :tenantId AND Pilot IN (SELECT * FROM [dbo].[fn_SplitString_cte] (:pilotId, ','))) " +
            "AND (:tenantId IS NULL OR LEN(:tenantId) = 0 OR tblCfg_Person.TenantID = :tenantId) " +
            "AND (:agentId IS NULL OR LEN(:agentId) = 0 OR tblCfg_Person.DBID IN " +
            "(SELECT * FROM [dbo].[fn_SplitString_cte] (:agentId, ','))))) ",
            nativeQuery = true)
    List<Object[]> queryServicePerformanceReportByEmailUsed(
            @Param("pilotId") String pilotId,
            @Param("tenantId") String tenantId,
            @Param("enable") String enable,
            @Param("agentId") String agentId);

    /**
     * 新增/更新專員代理人
     *
     * @param SubstituteIDs
     * @param DBID
     * @return success/fail
     */
    @Transactional
    @Modifying
    @Query(value = "update tblCfg_Person set SubstituteIDs = :subs where DBID = :agentid", nativeQuery = true)
    int updateSubstituteIDsByDBID(@Param("agentid") Long DBID, @Param("subs") String SubstituteIDs);

    /**
     * 依personid取得預設代理人
     *
     * @param DBID
     * @return
     */
    @Query(value = "select SubstituteIDs from tblCfg_Person with(nolock) where DBID = :agentid", nativeQuery = true)
    String findSubstituteIDsByDBID(@Param("agentid") Long DBID);

    /**
     * 依agentid取得所有所在的小組名稱
     *
     * @param agentID
     * @return
     */
    @Query(value = "select pilot.PilotName as pilots " +
            "from tblCfg_Pilot_Agent pilot_agent with(nolock) " +
            "inner join tblCfg_Pilot pilot on pilot.Pilot = pilot_agent.Pilot " +
            "where pilot_agent.AgentID = :agentID", nativeQuery = true)
    List<PilotNameDTO> findPilotsByAgentID(@Param("agentID") Long agentID);

    CfgPerson findByACCOUNT(String ACCOUNT);

    /**
     * 輸入多個小組取得在這些小組內專員的person物件
     *
     * @param pilots
     * @return
     */
    @Query(value = "select * from tblCfg_Person with(nolock) " +
            "where DBID in ( " +
            "select distinct AgentID from tblCfg_Pilot_Agent with(nolock) " +
            "where Pilot in (:pilots) " +
            ")", nativeQuery = true)
    Set<Long> findAllDBIDbyPilot(@Param("pilots") Set<Long> pilots);

    /**
     * 查詢不包含自己(yourSelfDBID)在內的複數DBID資訊  回傳person List
     *
     * @param DBIDs
     * @param yourSelfDBID
     * @return
     */
    @Query(value = "SELECT * FROM tblCfg_Person with(nolock) " +
            "where DBID IN :DBIDs " +
            "and STATE = 0 " +
            "and DBID != :yourSelfDBID " +
            "order by DN asc, ACCOUNT"
            , nativeQuery = true)
    List<CfgPerson> findAvailableByDBIDs(
            @Param("DBIDs") Set<Long> DBIDs,
            @Param("yourSelfDBID") Long yourSelfDBID
    );

    @Query(value = "SELECT *\n" +
            "from tblCfg_Person\n" +
            "WHERE ';' + SubstituteIDs + ';' LIKE '%;' + :substituteID + ';%'"
            , nativeQuery = true)
    List<CfgPerson> findAllByFuzzySubstituteID(
            @Param("substituteID") String substituteID
    );

    @Query(value = "SELECT * FROM tblCfg_Person WITH (NOLOCK)\n" +
            "WHERE STATE = '0'"
            , nativeQuery = true)
    List<CfgPerson> findAllState0();
}