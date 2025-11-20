package com.taoge.api.demo.orderList;

import com.github.pagehelper.PageInfo;
import com.taoge.biz.server.OrderServer;
import com.taoge.biz.server.param.order.OrderListParam;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.util.ServiceUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DemoOrderListController extends BaseController<DemoOrderListParam> {
    @Resource
    OrderServer orderServer;

    @Override
    @RequestMapping(value = "/api/demo/order/list", method = RequestMethod.POST)
    public ResponseData<?> execute(@RequestBody DemoOrderListParam param) {
        PageInfo pageInfo = orderServer.orderList(param.convertTo(OrderListParam.class));

        List<DemoOrderListVO> orderList;
        try {
            orderList = ServiceUtil.convertListToVO(pageInfo.getList(), DemoOrderListVO.class);
            pageInfo.setList(orderList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseData.success("", pageInfo);
    }
}
