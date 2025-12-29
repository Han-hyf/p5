/*
 * @ClassName UserVipService
 * @Description
 * @version 1.0
 * @Date 2023-11-06 21:28:21
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.UserVipMapper;
import com.taoge.biz.persistent.entity.UserVip;
import com.taoge.biz.persistent.entity.UserVipRecord;
import com.taoge.framework.service.BaseService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class UserVipService extends BaseService<UserVip, UserVipMapper> {
    /**
     * 激活vip
     *
     * @param userVipRecord vip购买记录
     */
    public void activateUserVip(UserVipRecord userVipRecord) {
        UserVip userVip = getMapper().getByUserId(userVipRecord.getUserId());
        Date expireDate;
        if (null == userVip) {
            userVip = new UserVip();
            expireDate = calcExpireDate(new Date(), userVipRecord.getVipDays());
        } else {
            expireDate = calcExpireDate(userVip.getExpireTime(), userVipRecord.getVipDays());
        }
        userVip.setUserId(userVipRecord.getUserId());
        userVip.setLevel(userVipRecord.getVipLevel());
        userVip.setLevelName(userVipRecord.getVipName());
        userVip.setType(userVipRecord.getVipDaysName());
        userVip.setExpireTime(expireDate);
        save(userVip);
    }

    private Date calcExpireDate(Date currentDate, Integer expireDays) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(currentDate);
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        return DateUtils.addDays(instance.getTime(), expireDays);
    }
}