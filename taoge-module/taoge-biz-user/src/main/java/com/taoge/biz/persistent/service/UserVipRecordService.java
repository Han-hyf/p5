/*
 * @ClassName UserVipRecordService
 * @Description
 * @version 1.0
 * @Date 2023-11-06 21:28:22
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.UserVipRecordMapper;
import com.taoge.biz.persistent.entity.UserVipRecord;
import com.taoge.framework.service.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserVipRecordService extends BaseService<UserVipRecord, UserVipRecordMapper> {
    /**
     * 生成购买vip记录订单
     */
    public UserVipRecord applyBuyVip(Long userId, String businessOrderSn, String vipName, BigDecimal vipPrice, String vipIcon, Integer vipDays, String vipDaysName, Integer vipLevel, BigDecimal payAmount) {
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

    /**
     * 支付成功
     *
     * @return true-支付成功 false-已更新过
     */
    public boolean paySuccess(String businessOrderSn) {
        return getMapper().paySuccess(businessOrderSn) == 1;
    }

    public UserVipRecord getByBusinessOrderSn(String businessOrderSn) {
        return getMapper().getByBusinessOrderSn(businessOrderSn);
    }
}