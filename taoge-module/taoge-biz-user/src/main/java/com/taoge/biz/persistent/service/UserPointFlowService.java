/*
 * @ClassName UserPointFlowService
 * @Description 
 * @version 1.0
 * @Date 2023-11-06 21:28:21
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.UserPointFlowMapper;
import com.taoge.biz.persistent.entity.UserPointFlow;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class UserPointFlowService extends BaseService<UserPointFlow ,UserPointFlowMapper> {
}