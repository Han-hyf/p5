/*
 * @ClassName UserAccountService
 * @Description 
 * @version 1.0
 * @Date 2023-11-06 21:28:21
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.UserAccountMapper;
import com.taoge.biz.persistent.entity.UserAccount;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService extends BaseService<UserAccount ,UserAccountMapper> {
}