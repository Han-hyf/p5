package com.taoge.biz.server;

import com.github.pagehelper.PageInfo;
import com.taoge.biz.server.param.order.OrderDetailParam;
import com.taoge.biz.server.param.order.OrderListParam;
import com.taoge.biz.server.vo.order.OrderDetailVO;
import com.taoge.biz.server.vo.order.OrderListVO;
import com.taoge.biz.persistent.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单服务
 */
@Service
public class OrderServer {
    @Resource
    OrderService orderService;

    /**
     * 分页查询订单列表
     *
     * @param param 查询参数
     */
    public PageInfo<OrderListVO> orderList(OrderListParam param) {
        return orderService.pageVOList(param, OrderListVO.class);
    }

    /**
     * 查询订单详情
     *
     * @param param 查询参数
     */
    public OrderDetailVO orderDetail(OrderDetailParam param) {
        return orderService.getVO(param.getOrderId(), OrderDetailVO.class);
    }
}
