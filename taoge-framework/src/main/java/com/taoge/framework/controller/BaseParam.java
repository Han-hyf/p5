package com.taoge.framework.controller;

import com.taoge.framework.annotation.Param;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.exception.ParamException;
import com.taoge.framework.util.BeanMapUtil;
import com.taoge.framework.util.ParamValidateUtil;

import java.util.HashMap;

/**
 * Created by xuejingtao
 */
@Param
public class BaseParam extends BasePO {

    public HashMap<String, Object> toMap() {
        return BeanMapUtil.toHashMap(this);
    }

    /**
     * 校验，返回校验结果
     */
    public ResponseData validateAndResult() {
        return ParamValidateUtil.validateParam(this);
    }

    /**
     * 校验，不通过抛异常
     */
    public void validate() {
        ResponseData responseData = validateAndResult();
        if (!responseData.isSuccess()) {
            throw new ParamException(responseData.getCode(), responseData.getMsg());
        }
    }

}
