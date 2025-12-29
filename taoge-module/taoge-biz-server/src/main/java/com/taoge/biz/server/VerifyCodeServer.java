package com.taoge.biz.server;

import com.taoge.biz.common.constant.SmsConstant;
import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.common.enums.VerifyCodeStatusEnum;
import com.taoge.biz.common.errorCode.SmsErrorCodeEnum;
import com.taoge.biz.common.errorCode.VerifyCodeErrorCodeEnum;
import com.taoge.biz.common.redis.SmsRedisKey;
import com.taoge.biz.persistent.entity.VerifyCode;
import com.taoge.biz.persistent.service.VerifyCodeService;
import com.taoge.biz.server.param.sms.SendSmsCodeParam;
import com.taoge.biz.server.param.sms.ValidateSmsCodeParam;
import com.taoge.biz.server.vo.sms.SmsResponse;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.exception.BusinessException;
import com.taoge.framework.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerifyCodeServer {
    @Resource
    SmsServer smsServer;
    @Resource
    VerifyCodeService verifyCodeService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    /**
     * 短信验证码长度，通常是4~6位，可以改成配置化
     */
    private static final int SMS_CODE_LENGTH = 4;

    /**
     * 发送短信验证码
     */
    public void sendSmsCode(SendSmsCodeParam param) {
        // 获取当前请求用户信息
        UserInfo userInfo = UserContext.get();

        // 防止连续点击，加一个短时间的锁
        String sendLockKey = SmsRedisKey.getSendLockKey(DigestUtils.md5DigestAsHex((userInfo.getUserId() + param.getOriginMobile()
                + param.getMobilePrefix() + param.getIso() + param.getActionType()
                + param.getIp()).getBytes(StandardCharsets.UTF_8)));
        Boolean exists = stringRedisTemplate.opsForValue().setIfAbsent(sendLockKey, "1", 3, TimeUnit.SECONDS);
        if (!(null != exists && exists)) {
            throw new BusinessException(SmsErrorCodeEnum.BUSINESS_CONTROL.getCode(), SmsErrorCodeEnum.BUSINESS_CONTROL.getMsg());
        }

        // 验证当前是否已经发送过短信（提示：您的短信已发送，请在xxx秒后再重试）
        // 当前用户、当前业务、当前手机号，是否已经发送过
        VerifyCode verifyCode = verifyCodeService.selectValidCode(userInfo.getUserId(),
                param.getMobilePrefix() + param.getOriginMobile(), param.getActionType());
        if (null != verifyCode) {
            // 过期时间： 21:53
            // 当前时间： 21:51
            if (verifyCode.getExpireTime().getTime() > System.currentTimeMillis()) {
                // 计算还有多久过期
                long s = (verifyCode.getExpireTime().getTime() - System.currentTimeMillis()) / 1000;
                throw new BusinessException(VerifyCodeErrorCodeEnum.SMS_CODE_SEND.getCode(), VerifyCodeErrorCodeEnum.SMS_CODE_SEND.getMsg() + ", 请" + s + "秒后重试");
            }
        }

        // 短信安全校验
        smsServer.validateSmsInfo(param);

        // 生成短信验证码
        String code = generateSmsCode();

        // 生成验证码记录
        verifyCode = saveVerifyCode(userInfo.getUserId(), param.getMobilePrefix() + param.getOriginMobile(), code, param.getActionType());

        // 调用发送短信服务
        SmsResponse smsResponse = smsServer.sendCodeSms(param, code);

        // 如果发送失败，修改验证码状态，给前端提示
        if (!smsResponse.isSuccess()) {
            VerifyCode update = new VerifyCode();
            update.setId(verifyCode.getId());
            update.setStatus(VerifyCodeStatusEnum.INVALID.name());
            verifyCodeService.updateByPrimaryKeySelective(update);
            throw new BusinessException(SmsErrorCodeEnum.SEND_MSG_ERROR.getCode(), SmsErrorCodeEnum.SEND_MSG_ERROR.getMsg());
        }
    }

    /**
     * 校验短信验证码
     */
    public void validateSmsCode(ValidateSmsCodeParam param) {
        // 获取当前请求用户信息
        UserInfo userInfo = UserContext.get();

        VerifyCode verifyCode = verifyCodeService.selectValidCode(userInfo.getUserId(),
                param.getMobilePrefix() + param.getOriginMobile(), param.getActionType());
        // 校验验证码是否存在
        if (null == verifyCode) {
            throw new BusinessException(VerifyCodeErrorCodeEnum.SMS_CODE_NOT_EXISTS.getCode(), VerifyCodeErrorCodeEnum.SMS_CODE_NOT_EXISTS.getMsg());
        }

        // 校验验证码是否过期
        if (verifyCode.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new BusinessException(VerifyCodeErrorCodeEnum.SMS_CODE_EXPIRE.getCode(), VerifyCodeErrorCodeEnum.SMS_CODE_EXPIRE.getMsg());
        }

        // 校验验证码是否正确
        if (!verifyCode.getCode().equals(param.getCode())) {
            // 允许2次输入错误
            if (verifyCode.getFailCount() < 2) {
                // 失败次数加1，状态改为验证错误
                verifyCodeService.validateWrong(verifyCode.getId());
                throw new BusinessException(VerifyCodeErrorCodeEnum.SMS_CODE_WRONG.getCode(), VerifyCodeErrorCodeEnum.SMS_CODE_WRONG.getMsg());
            } else {
                // 超过次数，修改验证码为验证失败
                verifyCodeService.validateFail(verifyCode.getId());
                throw new BusinessException(VerifyCodeErrorCodeEnum.SMS_CODE_FAIL.getCode(), VerifyCodeErrorCodeEnum.SMS_CODE_FAIL.getMsg());
            }
        }

        // 更新验证码状态为成功
        verifyCodeService.updateSuccess(verifyCode.getId());
    }

    /**
     * 保存短信验证码
     */
    private VerifyCode saveVerifyCode(Long userId, String mobile, String code, SmsActionType actionType) {
        // 查询是否有未验证的验证码
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setUserId(userId);
        verifyCode.setMobile(mobile);
        verifyCode.setCode(code);
        verifyCode.setActionType(actionType.name());
        verifyCode.setFailCount(0);
        verifyCode.setExpireTime(generateSmsCodeExpireTime(actionType));
        verifyCodeService.insertSelective(verifyCode);
        return verifyCode;
    }

    /**
     * 根据不同业务，计算短信验证码过期时间
     */
    private Date generateSmsCodeExpireTime(SmsActionType actionType) {
        return new Date((System.currentTimeMillis() + actionType.getExpireTime()));
    }

    /**
     * 生成短信验证码
     */
    private static String generateSmsCode() {
        // 如果是开发环境，直接返回约定默认的短信code
        if (SmsConstant.isEnvDev()) {
            return "1111";
        }
        // 随机4为数字，拼接到一起
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < SMS_CODE_LENGTH; i++) {
            code.append(RandomUtils.nextInt(0, 10));
        }
        log.info("smsCode:{}", code);
        return code.toString();
    }

    public static void main(String[] args) {
        System.out.println(generateSmsCode());
    }
}
