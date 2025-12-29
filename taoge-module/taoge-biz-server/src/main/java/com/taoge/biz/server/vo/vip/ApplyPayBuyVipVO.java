package com.taoge.biz.server.vo.vip;

import com.taoge.biz.common.enums.PayTypeEnum;
import com.taoge.biz.server.vo.order.ApplyAliOrderVO;
import com.taoge.biz.server.vo.order.ApplyWxOrderVO;
import com.taoge.framework.controller.BaseVO;
import lombok.Data;

/**
 * 创建支付订单
 */
@Data
public class ApplyPayBuyVipVO extends BaseVO {
    private PayTypeEnum payType;
    /**
     * 微信支付返回值
     */
    private ApplyWxOrderVO applyWxOrderVO;
    /**
     * 支付宝返回值
     */
    private ApplyAliOrderVO applyAliOrderVO;
}
