package com.taoge.api.demo.orderDetail;

import com.taoge.biz.server.OrderServer;
import com.taoge.biz.server.param.order.OrderDetailParam;
import com.taoge.biz.server.vo.order.OrderDetailVO;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoOrderDetailController extends BaseController<DemoOrderDetailParam> {

    @Resource
    OrderServer orderServer;

    @Override
    @PostMapping("/demo/order/detail")
    public ResponseData<DemoOrderDetailVO> execute(@RequestBody DemoOrderDetailParam param) {
        // 将api请求参数，转换成server层参数（底层服务参数可能很多，但前端接口接收的参数很少）
        // 底层服务可能提供10个查询条件，但接口层只需要按其中3个条件进行查询
        OrderDetailParam orderDetailParam = param.convertTo(OrderDetailParam.class);
        // 调用server层接口，查询订单详情
        OrderDetailVO orderDetailVO = orderServer.orderDetail(orderDetailParam);
        // 将server层接口返回数据，转换成对外api接口返回值（部分敏感字段、或者不需要展示的字段，不提供给前端）
        DemoOrderDetailVO demoOrderDetailVO = orderDetailVO.convertTo(DemoOrderDetailVO.class);
        return ResponseData.success("", demoOrderDetailVO);
    }
}
