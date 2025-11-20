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
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.exception.BusinessException;
import com.taoge.framework.util.SnowFlake;
import com.taoge.framework.util.UserContext;
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
    public BusinessOrder applyBusinessOrder(ApplyBusinessOrderParam param){

        //生成signKey:唯一标识：md5(user_id + business_type + business_param)
        String signKey = generateSignKey(param.getUserId(),param.getBusinessType().name(),param.getBusinessParam());
        BusinessOrder businessOrder = getInitBusinessOrderBySignKey(signKey);
        if (businessOrder != null){
            throw new BusinessException(OrderErrorCodeEnum.INIT_EXIST_ERROR.getCode(),OrderErrorCodeEnum.INIT_EXIST_ERROR.getMsg(),businessOrder);
        }

        //生成订单编号
        String businessOrderSn = generateBusinessOrderSn(param.getBusinessType());

        //调用方法保存订单记录
        businessOrder = businessOrderService.applyOrder(param.getUserId(), businessOrderSn, signKey, param.getTotalMoney(),
                param.getPayMoney(), param.getBusinessType().name(), param.getBusinessParam());
        return businessOrder;
    }



    /**
     * 生成signKey的方法
     */
    private String generateSignKey(Long userId, String businessType, String businessParam) {
         String signKey = DigestUtils.md5DigestAsHex((userId + businessType + businessParam).getBytes(StandardCharsets.UTF_8));
         return signKey;
    }

    /**
     * 生成订单编号的方法
     */
    private String generateBusinessOrderSn(BusinessTypeEnum businessTypeEnum) {
        String businessOrderSn = String.valueOf(SnowFlake.nextId());
        if (null != businessTypeEnum){
            switch (businessTypeEnum){
                case BUY_VIP:
                    return "BV" + businessOrderSn;
            }
        }

        return null;
    }


    /**
     * 根据sign Key查找订单
     */
    private BusinessOrder getInitBusinessOrderBySignKey(String signKey){

        return businessOrderService.getInitOrderBySignKey(signKey);
    }



    public ApplyWxOrderVO applyWxPay(PayOrderParam param) {

        //查询业务订单.创建微信支付订单信息,并关联业务订单编号
        BusinessOrder businessOrder = businessOrderService.getInitOrderByBusinessOrderSn(param.getBusinessOrderSn());
        if (businessOrder == null){
            throw new BusinessException(OrderErrorCodeEnum.BUSINESS_ORDER_NOT_EXISTS.getCode(),OrderErrorCodeEnum.BUSINESS_ORDER_NOT_EXISTS.getMsg());
        }
        //调用微信支付,返回支付配置信息,修改微信支付订单信息
        ApplyWxOrderVO applyWxOrderVO = new ApplyWxOrderVO();
        applyWxOrderVO.setBusinessOrderSn(businessOrder.getBusinessOrderSn());
        return applyWxOrderVO;

    }
    public ApplyAliOrderVO applyAliPay(PayOrderParam param) {

        //查询业务订单.创建微信支付订单信息,并关联业务订单编号
        BusinessOrder businessOrder = businessOrderService.getInitOrderByBusinessOrderSn(param.getBusinessOrderSn());
        if (businessOrder == null){
            throw new BusinessException(OrderErrorCodeEnum.BUSINESS_ORDER_NOT_EXISTS.getCode(),OrderErrorCodeEnum.BUSINESS_ORDER_NOT_EXISTS.getMsg());
        }
        //调用微信支付,返回支付配置信息,修改微信支付订单信息
        ApplyAliOrderVO applyAliOrderVO = new ApplyAliOrderVO();
        applyAliOrderVO.setBusinessOrderSn(businessOrder.getBusinessOrderSn());
        return applyAliOrderVO;

    }


    /**
     * 支付成功后
     */
    @Transactional
    public void paySuccess(String orderSn) {

        //根据微信支付订单,查询业务订单
        BusinessOrder businessOrder = businessOrderService.getInitOrderByBusinessOrderSn(orderSn);


        BusinessTypeEnum businessType = BusinessTypeEnum.getByBusinessType(businessOrder.getBusinessType());
        if (businessType == null){
            //发送飞书预警
            log.error("paySuccess error,  businessType not exist, businessOrderSn:{}",businessOrder.getBusinessOrderSn());
            return;

        }

        // 更新业务订单状况,INIT转为付款成功,在 BusinessOrderService 中
        if (!businessOrderService.paySuccess(businessOrder.getBusinessOrderSn())){
            //如果没有更新成功,发送飞书预警
            log.error("businessOrder paySuccess error, businessOrderSn:{}",businessOrder.getBusinessOrderSn());
            return;
        }

        switch (businessType){
            case BUY_VIP:

                OrderSnParam orderSnParam= new OrderSnParam();
                orderSnParam.setBusinessOrderSn(orderSn);
                userAccountServer.buyVipSuccess(orderSnParam);
                break;
        }



        // 激活用户会员,



    }
}
