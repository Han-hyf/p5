package com.taoge.biz.server;

import com.taoge.biz.persistent.entity.BusinessOrder;
import com.taoge.biz.persistent.service.BusinessOrderService;
import com.taoge.biz.server.param.notify.WxPayNotifyParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NotifyServer {

    @Resource
    BusinessOrderServer businessOrderServer;

    public void wxPayNotify(WxPayNotifyParam param) {
        // TODO 保存回调信息

        // TODO 发送mq
    }

    /**
     * 从mq中拿到微信支付回调
     */
    public void wxPayCallback(WxPayNotifyParam param) {
        // 根据 wxOrderSn 查询微信支付订单
        // 更新微信订单状态（幂等）

        // 根据业务订单，处理回调
        businessOrderServer.paySuccess(param.getBusinessOrderSn());
    }
}
