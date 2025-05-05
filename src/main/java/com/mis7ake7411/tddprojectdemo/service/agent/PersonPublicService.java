package com.mis7ake7411.tddprojectdemo.service.agent;

import com.mis7ake7411.tddprojectdemo.entity.CfgPerson;
import com.mis7ake7411.tddprojectdemo.repository.CfgPersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonPublicService {

    @Autowired
    private CfgPersonRepository cfg_PersonRepository;

    @Cacheable(value = "getAllPersonDataCache")
    public Map<Long, CfgPerson> getAllPersonData() {
        //啟動時先取得 Person 資訊
        List<CfgPerson> personList = cfg_PersonRepository.findAllPerson();
        return personList
                .stream()
                .collect(Collectors.toMap(
                        CfgPerson::getDBID,
                        data -> data
                ));
    }

    @Cacheable(value = "getPersonDNAndAccount", key = "#personID")
    public String getPersonDNAndAccount(Long personID) {
        if (personID == null) {
            log.info("personID not found in map ");
            return "";
        }
        CfgPerson person = getAllPersonData().get(personID);
        if (person == null || person.getDN() == null) {
            person = cfg_PersonRepository.findByDbId(String.valueOf(personID));
            log.info("person is null, personID: " + personID);
        }

        return formatUserName(person);
    }

    public String formatUserName(CfgPerson person) {
        //人員ID從人員資料表沒找到資料，回傳空值
        if (person == null) {
            return "";
        }
        //依據 personID 取得分機號碼與帳號名稱
        String dN = person.getDN();
        String user_name = person.getUSER_NAME();
        return ((dN == null || dN.trim().isEmpty()) ? "-" : dN) + "(" + user_name + ")";
    }

    // 改成除了personEntity外，把分機+名子的key也清掉
    // 假設改動分機或名子而不去清分機+名子的key，redis在期限內不會去取新的
    @Caching(evict = {
            @CacheEvict(value = "getAllPersonDataCache", allEntries = true),
            @CacheEvict(value = "getPersonDNAndAccount", key = "#person.getDBID()")
    })
    public void insertAndUpdatePersonData(CfgPerson person) {
        //寫入或修改人員資訊
        cfg_PersonRepository.save(person);
    }
    // 改成除了personEntity外，改成把分機+名子的key也清掉
    // 假設改動分機或名子而不去清分機+名子的key，redis在期限內不會去取新的
    @Caching(evict = {
            @CacheEvict(value = "getAllPersonDataCache", allEntries = true),
            @CacheEvict(value = "getPersonDNAndAccount", key = "#person.getDBID()")
    })
    public void insertAndUpdatePersonData(List<CfgPerson> personList) {
        //寫入或修改人員資訊
        cfg_PersonRepository.saveAll(personList);
    }
    // 改成除了personEntity外，改成把分機+名子的key也清掉
    @Caching(evict = {
            @CacheEvict(value = "getAllPersonDataCache", key = "#personID"),
            @CacheEvict(value = "getPersonDNAndAccount", key = "#personID")
    })
    public void evictPersonDataCache(Long personID) {
        log.info("CacheEvict personID: " + personID);
        // 此方法用於從緩存中刪除特定的 personID
    }

}
