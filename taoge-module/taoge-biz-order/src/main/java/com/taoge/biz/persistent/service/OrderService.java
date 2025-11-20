/*
 * @ClassName OrderService
 * @Description 
 * @version 1.0
 * @Date 2023-10-29 18:10:50
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.OrderMapper;
import com.taoge.biz.persistent.entity.Order;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends BaseService<Order ,OrderMapper> {
}