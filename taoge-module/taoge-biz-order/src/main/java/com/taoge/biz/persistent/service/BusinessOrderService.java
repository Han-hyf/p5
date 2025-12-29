/*
 * @ClassName BusinessOrderService
 * @Description
 * @version 1.0
 * @Date 2023-11-07 20:17:12
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.BusinessOrderMapper;
import com.taoge.biz.persistent.entity.BusinessOrder;
import com.taoge.framework.service.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BusinessOrderService extends BaseService<BusinessOrder, BusinessOrderMapper> {

    /**
     * 生成订单
     */
    public BusinessOrder applyOrder(Long userId, String businessOrderSn, String signKey, BigDecimal totalMoney, BigDecimal payMoney, String businessType, String businessParam) {
        BusinessOrder businessOrder = new BusinessOrder();
        businessOrder.setUserId(userId);
        businessOrder.setBusinessOrderSn(businessOrderSn);
        businessOrder.setSignKey(signKey);
        businessOrder.setTotalMoney(totalMoney);
        businessOrder.setPayMoney(payMoney);
        businessOrder.setBusinessType(businessType);
        businessOrder.setBusinessParam(businessParam);
        insertSelective(businessOrder);
        return businessOrder;
    }

    /**
     * 查询待支付订单
     */
    public BusinessOrder getInitOrderBySignKey(String signKey) {
        return getMapper().getInitOrderBySignKey(signKey);
    }

    /**
     * 根据订单编号查询
     */
    public BusinessOrder getByBusinessOrderSn(String businessOrderSn) {
        return getMapper().getByBusinessOrderSn(businessOrderSn);
    }

    /**
     * 订单支付成功
     *
     * @return true-成功 false-表示已经更新过
     */
    public boolean paySuccess(String businessOrderSn) {
        return getMapper().paySuccess(businessOrderSn) == 1;
    }


}