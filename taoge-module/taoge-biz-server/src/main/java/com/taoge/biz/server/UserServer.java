package com.taoge.biz.server;

import com.taoge.biz.common.errorCode.UserErrorCodeEnum;
import com.taoge.biz.common.util.PasswordUtil;
import com.taoge.biz.persistent.entity.UserBase;
import com.taoge.biz.persistent.service.UserAuthService;
import com.taoge.biz.persistent.service.UserBaseService;
import com.taoge.biz.persistent.service.UserInfoService;
import com.taoge.biz.server.param.user.UpdateUserInfoParam;
import com.taoge.biz.server.param.user.UserLoginByMobileParam;
import com.taoge.biz.server.param.user.UserLoginByUsernameParam;
import com.taoge.biz.server.param.user.UserRegisterParam;
import com.taoge.biz.server.vo.user.UserLoginVO;
import com.taoge.biz.server.vo.user.UserRegisterVO;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.exception.BusinessException;
import com.taoge.framework.util.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserServer {
    @Resource
    UserBaseService userBaseService;
    @Resource
    UserAuthService userAuthService;
    @Resource
    UserInfoService userInfoService;

    /**
     * 校验手机号是否已经注册
     */
    public boolean validateMobileIsRegister(String mobile) {
        UserBase userBase = userBaseService.getByMobile(mobile);
        return null != userBase;
    }

    /**
     * 用户注册
     */
    @Transactional
    public UserRegisterVO register(UserRegisterParam param) {
        // 校验用户名是否存在
        UserBase userBase = userBaseService.getByUsername(param.getUsername());
        if (null != userBase) {
            throw new BusinessException(UserErrorCodeEnum.USERNAME_EXISTS_ERROR.getCode(), UserErrorCodeEnum.USERNAME_EXISTS_ERROR.getMsg());
        }

        // 创建user_base记录
        userBase = userBaseService.register(param.getUsername(), param.getPassword(), param.getMobile());

        // 创建user_info记录
        userInfoService.register(userBase.getId());

        // 创建user_auth记录
        userAuthService.register(userBase.getId());

        // 返回注册信息
        UserRegisterVO userRegisterVO = new UserRegisterVO();
        userRegisterVO.setUserId(userBase.getId());
        return userRegisterVO;
    }

    /**
     * 手机号登录
     */
    public UserLoginVO loginByMobile(UserLoginByMobileParam param) {
        // 按手机号查询账户
        UserBase userBase = userBaseService.getByMobile(param.getMobile());
        if (null == userBase) {
            throw new BusinessException(UserErrorCodeEnum.MOBILE_NOT_EXISTS_ERROR.getCode(), UserErrorCodeEnum.MOBILE_NOT_EXISTS_ERROR.getMsg());
        }
        // 登录成功
        return loginSuccess(userBase);
    }

    /**
     * 账号密码登录
     */
    public UserLoginVO loginByUsername(UserLoginByUsernameParam param) {
        // 校验账号是否存在
        UserBase userBase = userBaseService.getByUsername(param.getUsername());
        if (null == userBase) {
            throw new BusinessException(UserErrorCodeEnum.USERNAME_LOGIN_ERROR.getCode(), UserErrorCodeEnum.USERNAME_LOGIN_ERROR.getMsg());
        }
        // 校验密码是否正确
        String password = PasswordUtil.encryptionPassword(param.getPassword(), userBase.getSalt());
        if (!password.equals(userBase.getPassword())) {
            throw new BusinessException(UserErrorCodeEnum.USERNAME_LOGIN_ERROR.getCode(), UserErrorCodeEnum.USERNAME_LOGIN_ERROR.getMsg());
        }
        // 登录成功
        return loginSuccess(userBase);
    }

    /**
     * 登陆成功
     */
    private UserLoginVO loginSuccess(UserBase userBase) {
        // 记录最后一次登录时间
        userBaseService.loginSuccess(userBase.getId());

        // 登录成功
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUserId(userBase.getId());
        return userLoginVO;
    }

    /**
     * 修改用户信息
     */
    public void updateUserInfo(UpdateUserInfoParam param) {
        if (null == param.getNickname() && null == param.getBirthday() && null == param.getGender()) {
            return;
        }
        UserInfo userInfo = UserContext.get();
        userInfoService.updateUserInfo(userInfo.getUserId(), param.getNickname(), param.getGender().getGender(), param.getBirthday());
    }
}
