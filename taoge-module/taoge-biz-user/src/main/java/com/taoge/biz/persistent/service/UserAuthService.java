/*
 * @ClassName UserAuthService
 * @Description 
 * @version 1.0
 * @Date 2023-11-02 20:11:42
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.UserAuthMapper;
import com.taoge.biz.persistent.entity.UserAuth;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService extends BaseService<UserAuth ,UserAuthMapper> {
    /**
     * 用户注册
     */
    public void register(Long id) {
        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(id);
        insertSelective(userAuth);
    }
}