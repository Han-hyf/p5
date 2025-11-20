package com.taoge.biz.server;

import com.taoge.biz.common.errorCode.UserErrorCodeEnum;
import com.taoge.biz.common.utils.PasswordUtils;
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
import com.taoge.framework.exception.BaseException;
import com.taoge.framework.util.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserServer {

    @Resource
    UserBaseService userBaseService;
    @Resource
    UserInfoService userInfoService;
    @Resource
    UserAuthService userAuthService;

    /**
     * 校验手机号是否已经被注册
     * @return
     */
    public boolean validateMobileIsRegister(String mobile){
        //根据手机号查找账号
        UserBase userBase = userBaseService.getByMobile(mobile);
        return null!= userBase;

    }

    /**
     * 用户注册
     */

    @Transactional
    public UserRegisterVO register(UserRegisterParam param){
        // 校验用户输入的用户名是否已经存在
        UserBase userBase = userBaseService.getByUsername(param.getUsername());
        if (null != userBase){
            throw new BaseException(UserErrorCodeEnum.USERNAME_EXISTS_ERROR.getCode(),UserErrorCodeEnum.USERNAME_EXISTS_ERROR.getMsg());
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
     * 手机号登陆
     */
    public UserLoginVO loginByMobile(UserLoginByMobileParam param){
        //检查手机号是否已经注册
        UserBase userBase = userBaseService.getByMobile(param.getMobile());
        if (null == userBase){
            throw new BaseException(UserErrorCodeEnum.MOBILE_NOT_EXISTS_ERROR.getCode(),UserErrorCodeEnum.MOBILE_NOT_EXISTS_ERROR.getMsg());
        }
        //记录最后一次登录时间
        userBaseService.loginSuccess(userBase.getId());
        //登录成功,返回VO
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUserId(userBase.getId());
        return userLoginVO;

    }

    /**
     * 账号密码登录
     */
    public UserLoginVO loginByUsername(UserLoginByUsernameParam param){
        //检查账号是否存在(根据用户名搜索数据库)
        UserBase userBase = userBaseService.getByUsername(param.getUsername());
        if (null == userBase){
            throw new BaseException(UserErrorCodeEnum.MOBILE_NOT_EXISTS_ERROR.getCode(),UserErrorCodeEnum.MOBILE_NOT_EXISTS_ERROR.getMsg());
        }

        //检查密码
        String password = PasswordUtils.encryptionPassword(param.getPassword(),userBase.getSalt());
        if (!password.equals(userBase.getPassword())){
            throw new BaseException(UserErrorCodeEnum.PASSWORD_WRONG_ERROR.getCode(),UserErrorCodeEnum.PASSWORD_WRONG_ERROR.getMsg());
        }
        //记录最后一次登录时间
        userBaseService.loginSuccess(userBase.getId());
        //登录成功,返回VO
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUserId(userBase.getId());
        return userLoginVO;

    }

    public void updateUserInfo(UpdateUserInfoParam param){
        if (param.getNickname() == null && param.getGender() == null && param.getBirthday() == null){
            return;
        }
        UserInfo userInfo = UserContext.get();
        userInfoService.updateUserInfo(userInfo.getUserId(),param.getNickname(),param.getGender().getGender(),param.getBirthday());

    }

}
