package com.taoge.biz.server;

import com.taoge.biz.common.enums.BusinessTypeEnum;
import com.taoge.biz.common.errorCode.OrderErrorCodeEnum;
import com.taoge.biz.common.param.OrderSnParam;
import com.taoge.biz.persistent.entity.BusinessOrder;
import com.taoge.biz.persistent.service.BusinessOrderService;
import com.taoge.biz.persistent.service.UserVipRecordService;
import com.taoge.biz.server.param.order.ApplyBusinessOrderParam;
import com.taoge.biz.server.param.order.PayOrderParam;
import com.taoge.biz.server.vo.order.ApplyAliOrderVO;
import com.taoge.biz.server.vo.order.ApplyWxOrderVO;
import com.taoge.framework.exception.BusinessException;
import com.taoge.framework.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class BusinessOrderServer {
    @Resource
    BusinessOrderService businessOrderService;
    @Resource
    UserAccountServer userAccountServer;

    /**
     * 生成业务订单
     */
    public BusinessOrder applyBusinessOrder(ApplyBusinessOrderParam param) {
        // 唯一标识：md5(user_id + business_type + business_param)
        String signKey = generateSignKey(param.getUserId(), param.getBusinessType().name(), param.getBusinessParam());
        // 判断是否已有相同待支付订单
        BusinessOrder businessOrder = getInitBusinessOrderBySignKey(signKey);
        if (null != businessOrder) {
            throw new BusinessException(OrderErrorCodeEnum.INIT_ORDER_EXISTS.getCode(), OrderErrorCodeEnum.INIT_ORDER_EXISTS.getMsg(), businessOrder);
        }

        String businessOrderSn = generateBusinessOrderSn(param.getBusinessType());
        businessOrder = businessOrderService.applyOrder(param.getUserId(), businessOrderSn, signKey, param.getTotalMoney(),
                param.getPayMoney(), param.getBusinessType().name(), param.getBusinessParam());

        return businessOrder;
    }

    /**
     * 生成 唯一标识：md5(user_id + business_type + business_param)
     */
    private String generateSignKey(Long userId, String businessType, String businessParam) {
        return DigestUtils.md5DigestAsHex((userId + businessType + businessParam).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成订单编号
     */
    private String generateBusinessOrderSn(BusinessTypeEnum businessType) {
        String businessOrderSn = String.valueOf(SnowFlake.nextId());
        if (null != businessType) {
            switch (businessType) {
                case BUY_VIP:
                    return "BV" + businessOrderSn;
            }
        }
        return businessOrderSn;
    }

    private BusinessOrder getInitBusinessOrderBySignKey(String signKey) {
        return businessOrderService.getInitOrderBySignKey(signKey);
    }

    public ApplyWxOrderVO applyWxPay(PayOrderParam param) {
        // 查询业务订单
        BusinessOrder businessOrder = businessOrderService.getByBusinessOrderSn(param.getBusinessOrderSn());
        if (null == businessOrder) {
            throw new BusinessException(OrderErrorCodeEnum.BUSINESS_ORDER_NOT_EXISTS.getCode(), OrderErrorCodeEnum.BUSINESS_ORDER_NOT_EXISTS.getMsg());
        }

        // TODO 创建微信支付订单信息，并关联业务订单编号

        // TODO 调用微信支付，返回支付配置信息，修改微信支付订单信息
        ApplyWxOrderVO applyWxOrderVO = new ApplyWxOrderVO();
        applyWxOrderVO.setBusinessOrderSn(businessOrder.getBusinessOrderSn());

        return applyWxOrderVO;
    }

    public ApplyAliOrderVO applyAliPay(PayOrderParam param) {
        // 查询业务订单
        BusinessOrder businessOrder = businessOrderService.getByBusinessOrderSn(param.getBusinessOrderSn());
        if (null == businessOrder) {
            throw new BusinessException(OrderErrorCodeEnum.BUSINESS_ORDER_NOT_EXISTS.getCode(), OrderErrorCodeEnum.BUSINESS_ORDER_NOT_EXISTS.getMsg());
        }

        // TODO 创建支付宝支付订单信息，并关联业务订单编号

        // TODO 调用支付宝支付，返回支付配置信息，修改支付宝支付订单信息
        ApplyAliOrderVO applyAliOrderVO = new ApplyAliOrderVO();
        applyAliOrderVO.setBusinessOrderSn(businessOrder.getBusinessOrderSn());
        return applyAliOrderVO;
    }

    @Transactional
    public void paySuccess(String orderSn) {
        // 根据微信支付订单，查询业务订单
        BusinessOrder businessOrder = businessOrderService.getByBusinessOrderSn(orderSn);

        BusinessTypeEnum businessType = BusinessTypeEnum.getByBusinessType(businessOrder.getBusinessType());
        if (null == businessType) {
            log.error("paySuccess error, businessType not exists, businessOrderSn:{}", businessOrder.getBusinessOrderSn());
            // 发送飞书预警
            return;
        }

        //3. 更新业务订单状态（幂等）
        if (!businessOrderService.paySuccess(businessOrder.getBusinessOrderSn())) {
            log.error("businessOrder paySuccess error, businessOrderSn:{}", businessOrder.getBusinessOrderSn());
            // 发送飞书预警
            return;
        }

        switch (businessType) {
            case BUY_VIP:
                OrderSnParam orderSnParam = new OrderSnParam();
                orderSnParam.setOrderSn(orderSn);
                userAccountServer.buyVipSuccess(orderSnParam);
                break;
        }

    }
}
