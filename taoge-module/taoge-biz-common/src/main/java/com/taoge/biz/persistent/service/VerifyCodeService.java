/*
 * @ClassName VerifyCodeService
 * @Description
 * @version 1.0
 * @Date 2023-10-31 21:36:54
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.common.enums.VerifyCodeStatusEnum;
import com.taoge.biz.persistent.dao.VerifyCodeMapper;
import com.taoge.biz.persistent.entity.VerifyCode;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VerifyCodeService extends BaseService<VerifyCode, VerifyCodeMapper> {

    /**
     * 查询待验证code
     */
    public VerifyCode selectValidCode(Long userId, String mobile, SmsActionType actionType) {
        List<String> statusList = new ArrayList<>();
        statusList.add(VerifyCodeStatusEnum.INIT.name());
        statusList.add(VerifyCodeStatusEnum.WRONG.name());
        return getMapper().selectValidCode(userId, mobile, actionType.name(), statusList);
    }

    /**
     * 验证错误
     */
    public int validateWrong(Long id) {
        return getMapper().validateFail(id, VerifyCodeStatusEnum.WRONG.name());
    }

    /**
     * 验证失败
     */
    public int validateFail(Long id) {
        return getMapper().validateFail(id, VerifyCodeStatusEnum.FAIL.name());
    }

    /**
     * 验证成功
     */
    public int updateSuccess(Long id) {
        VerifyCode update = new VerifyCode();
        update.setId(id);
        update.setStatus(VerifyCodeStatusEnum.SUCCESS.name());
        return updateByPrimaryKeySelective(update);
    }
}