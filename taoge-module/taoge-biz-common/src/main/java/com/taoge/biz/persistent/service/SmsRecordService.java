/*
 * @ClassName SmsRecordService
 * @Description
 * @version 1.0
 * @Date 2023-10-31 20:28:12
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.persistent.dao.SmsRecordMapper;
import com.taoge.biz.persistent.entity.SmsRecord;
import com.taoge.framework.service.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public class SmsRecordService extends BaseService<SmsRecord, SmsRecordMapper> {
    /**
     * 统计用户、手机号、当天发送次数
     */
    public Long countByMobileInDay(Long userId, String mobile, SmsActionType actionType) {
        return getMapper().countByMobileInDay(userId, mobile, actionType.name());
    }

}