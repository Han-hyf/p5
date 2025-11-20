/*
 * @ClassName BusinessOrderService
 * @Description 
 * @version 1.0
 * @Date 2025-11-10 14:03:16
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.BusinessOrderMapper;
import com.taoge.biz.persistent.entity.BusinessOrder;
import com.taoge.framework.service.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class BusinessOrderService extends BaseService<BusinessOrder ,BusinessOrderMapper> {

    public BusinessOrder applyOrder(Long userId, String businessOrderSn, String signKey, BigDecimal totalMoney, BigDecimal payMoney,
                                    String businessType, String businessParam){
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
    public BusinessOrder getInitOrderBySignKey(String signKey){

        return getMapper().getInitOrderBySignKey(signKey);
    }


    /**
     * 根据订单编号查询

     */
    public BusinessOrder getInitOrderByBusinessOrderSn(String businessSn) {
        return getMapper().getInitOrderByBusinessOrderSn(businessSn);
    }

    /**
     * 订单支付成功
     * @return 1-成功 0-表示已经更新过
     */
    public Boolean paySuccess(String businessOrderSn){
        return getMapper().paySuccess(businessOrderSn) == 1;
    }
}