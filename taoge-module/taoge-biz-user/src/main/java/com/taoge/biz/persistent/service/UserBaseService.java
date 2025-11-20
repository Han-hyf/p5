/*
 * @ClassName UserBaseService
 * @Description
 * @version 1.0
 * @Date 2023-11-02 20:11:42
 */
package com.taoge.biz.persistent.service;


import com.taoge.biz.common.utils.PasswordUtils;
import com.taoge.biz.persistent.dao.UserBaseMapper;
import com.taoge.biz.persistent.entity.UserBase;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserBaseService extends BaseService<UserBase, UserBaseMapper> {

    /**
     * 按手机号查找用户
     */
    public UserBase getByMobile(String mobile) {
        return getMapper().getByMobile(mobile);
    }

    /**
     * 按用户名查找用户
     */
    public UserBase getByUsername(String username) {
        return getMapper().getByUsername(username);
    }

    /**
     * 用户注册
     */
    public UserBase register(String username, String password, String mobile) {
        UserBase userBase = new UserBase();
        userBase.setUsername(username);
        // 生成salt方法
        String salt = PasswordUtils.generateSalt();
        // 密码需要加密，抽取加密方法 md5(password + salt)
        password = PasswordUtils.encryptionPassword(password, salt);
        userBase.setPassword(password);
        userBase.setSalt(salt);
        userBase.setMobile(mobile);
        insertSelective(userBase);
        return userBase;
    }

    /**
     * 登录成功，记录最后一次登录时间
     */
    public void loginSuccess(Long userId) {
        UserBase update = new UserBase();
        update.setId(userId);
        update.setLastLoginTime(new Date());
        updateByPrimaryKeySelective(update);
    }
}