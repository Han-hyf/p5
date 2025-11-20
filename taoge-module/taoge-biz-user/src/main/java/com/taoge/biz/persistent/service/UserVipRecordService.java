/*
 * @ClassName UserVipRecordService
 * @Description 
 * @version 1.0
 * @Date 2025-11-06 17:52:35
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.UserVipRecordMapper;
import com.taoge.biz.persistent.entity.UserVipRecord;
import com.taoge.framework.service.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserVipRecordService extends BaseService<UserVipRecord ,UserVipRecordMapper> {
    /**
     * 生成购买vip
     *
     * @return
     */
    public UserVipRecord appBuyVip(Long userId, String businessOrderSn, String vipName, BigDecimal vipPrice, String vipIcon,
                                   Integer vipDays, String vipDaysName, Integer vipLevel, BigDecimal payAmount) {

        UserVipRecord userVipRecord = new UserVipRecord();
        userVipRecord.setUserId(userId);
        userVipRecord.setBusinessOrderSn(businessOrderSn);
        userVipRecord.setVipName(vipName);
        userVipRecord.setVipPrice(vipPrice);
        userVipRecord.setVipIcon(vipIcon);
        userVipRecord.setVipDays(vipDays);
        userVipRecord.setVipDaysName(vipDaysName);
        userVipRecord.setVipLevel(vipLevel);
        userVipRecord.setPayAmount(payAmount);
        insertSelective(userVipRecord);
        return userVipRecord;


    }

    public Boolean paySuccess(String businessOrderSn) {
        return getMapper().paySuccess(businessOrderSn) == 1;
    }

    public UserVipRecord getByBusinessOrderSn(String businessOrderSn){
        return getMapper().getByBusinessOrderSn(businessOrderSn);
    }
}