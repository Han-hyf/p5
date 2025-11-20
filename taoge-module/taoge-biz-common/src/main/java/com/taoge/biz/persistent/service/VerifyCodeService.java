/*
 * @ClassName VerifyCodeService
 * @Description 
 * @version 1.0
 * @Date 2025-10-23 16:05:19
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.persistent.dao.VerifyCodeMapper;
import com.taoge.biz.persistent.entity.VerifyCode;
import com.taoge.framework.service.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VerifyCodeService extends BaseService<VerifyCode ,VerifyCodeMapper> {


    /**
     * 查询待验证code
     */
    public VerifyCode selectValidCode(Long userId, String mobile, SmsActionType actionType){

        return getMapper().selectValidCode(userId, mobile, actionType.name());
    }


    /**
     * 更新已过期的code的status
     * @param userId
     * @param mobile
     * @param actionType
     * @return
     */
    public int updateExpireCode(Long userId,String mobile,SmsActionType actionType){
        return getMapper().updateExpireCode(userId,mobile, actionType.name());
    }

}