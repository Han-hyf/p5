/*
 * @ClassName UserAccountService
 * @Description 
 * @version 1.0
 * @Date 2025-11-06 17:52:35
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.UserAccountMapper;
import com.taoge.biz.persistent.entity.UserAccount;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService extends BaseService<UserAccount ,UserAccountMapper> {
}