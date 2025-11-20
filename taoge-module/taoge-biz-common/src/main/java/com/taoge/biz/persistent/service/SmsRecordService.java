/*
 * @ClassName SmsRecordService
 * @Description 
 * @version 1.0
 * @Date 2025-10-23 16:05:19
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.persistent.dao.SmsRecordMapper;
import com.taoge.biz.persistent.entity.SmsRecord;
import com.taoge.framework.service.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SmsRecordService extends BaseService<SmsRecord ,SmsRecordMapper> {


   public Long countByMobileInDay(Long userId, String mobile, SmsActionType actionType){
       return getMapper().countByMobileInDay(userId, mobile,  actionType.name());
   }

}