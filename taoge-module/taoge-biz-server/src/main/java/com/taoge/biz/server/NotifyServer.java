package com.taoge.biz.server;

import com.taoge.biz.common.param.OrderSnParam;
import com.taoge.biz.persistent.entity.BusinessOrder;
import com.taoge.biz.persistent.service.BusinessOrderService;
import com.taoge.biz.server.param.notify.WxPayNotifyParam;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

@Service
public class NotifyServer {

    @Resource
    BusinessOrderServer businessOrderServer;
    @Resource
    BusinessOrderService businessOrderService;


    public void wxPayNotify(WxPayNotifyParam param){

        //todo 保存回调信息

        //todo 发送mq
    }


    /**
     * 从mq中拿到微信支付回调
     * @param param
     */
    public void wxPayCallBack(WxPayNotifyParam param){
        //根据 wxOrderSn 查询微信支付订单

        //更新订单状态


        //根据业务订单,处理回调

        businessOrderServer.paySuccess(param.getBusinessOrderSn());
    }

}
