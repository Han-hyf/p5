/*
 * @ClassName UserInfoService
 * @Description
 * @version 1.0
 * @Date 2023-11-02 20:11:42
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.UserInfoMapper;
import com.taoge.biz.persistent.entity.UserInfo;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserInfoService extends BaseService<UserInfo, UserInfoMapper> {
    /**
     * 用户注册
     */
    public void register(Long id) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(id);
        userInfo.setNickname("昵称" + System.currentTimeMillis());
        insertSelective(userInfo);
    }

    /**
     * 更新用户信息
     */
    public int updateUserInfo(Long userId, String nickname, Integer gender, Date birthday) {
        return getMapper().updateUserInfo(userId, nickname, gender, birthday);
    }


}