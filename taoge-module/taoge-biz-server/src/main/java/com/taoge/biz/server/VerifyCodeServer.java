package com.taoge.biz.server;

import com.taoge.biz.common.enums.VerifyCodeStatusEnum;
import com.taoge.biz.common.errorCode.SmsErrorCodeEnum;
import com.taoge.biz.common.errorCode.VerifyCodeErrorCodeEnum;
import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.common.redis.SmsRedisKey;
import com.taoge.biz.persistent.entity.VerifyCode;
import com.taoge.biz.persistent.service.SmsRecordService;
import com.taoge.biz.persistent.service.VerifyCodeService;
import com.taoge.biz.server.param.sms.SendSmsCodeParam;
import com.taoge.biz.server.param.sms.ValidateSmsCodeParam;
import com.taoge.biz.server.vo.sms.SmsResponse;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.exception.BusinessException;
import com.taoge.framework.util.UserContext;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class VerifyCodeServer {
    @Resource
    SmsServer smsServer;
    @Resource
    VerifyCodeService verifyCodeService;
    @Resource
    SmsRecordService smsRecordService;
    @Resource
    StringRedisTemplate stringRedisTemplate;


    /**
     * 定义验证码过期时间
     */
    private static final long SMS_CODE_EXPIER_TIME =180 * 1000L;

    /**
     * 短信验证码长度
     */
    private static final int SMS_CODE_LENGTH = 4;
    private static final int SMS_DAY_MAX = 3;


    public void sendSmsCode(SendSmsCodeParam param,String templateParam){
        //获取当前用户信息
        //userId等可以直接在userContext中获取
        UserInfo userInfo = UserContext.get();

        //防止连续点击，加一个短时间的锁
        String sendLockKey = SmsRedisKey.getSendLockKey(DigestUtils.md5DigestAsHex((userInfo.getUserId() + param.getOriginMobile()
                + param.getMobilePrefix() + param.getIso() + param.getActionType()
                + param.getIp()).getBytes(StandardCharsets.UTF_8)));
        Boolean exists = stringRedisTemplate.opsForValue().setIfAbsent(sendLockKey,"1", 3,TimeUnit.SECONDS);
        if (!(null!=exists && exists)){
            throw new BusinessException(SmsErrorCodeEnum.BUSINESS_CONTROL.getCode(),SmsErrorCodeEnum.BUSINESS_CONTROL.getMsg());
        }


        // 验证是否已经发送过短信（提示：您的 短信已发送，请在xxx秒后重试）
        // 当前用户、当前业务、当前手机号，是否发送过
        VerifyCode verifyCode = verifyCodeService.selectValidCode(userInfo.getUserId(), param.getOriginMobile(), param.getActionType());
        if(verifyCode != null){
            if(verifyCode.getExpireTime().getTime() > System.currentTimeMillis()){
                long s = (verifyCode.getExpireTime().getTime() - System.currentTimeMillis())/1000;
                throw new BusinessException(VerifyCodeErrorCodeEnum.SMS_CODE_SEND.getCode(),
                        VerifyCodeErrorCodeEnum.SMS_CODE_SEND.getMsg() + ",请" + s + "秒后重试");
            }
        }

        // 短信安全校验
        smsServer.validateSmsInfo(param);

        //生成验证码
        String code = generateSmsCode();

        //保存生成验证码的记录
        verifyCode = saveVerifyCode(userInfo.getUserId(), param.getOriginMobile(), code, param.getActionType());

        //调用SmsServer发送短信
        SmsResponse smsResponse = smsServer.sendCodeSms(param, code);

        //如果发送失败，更新验证码状态
        if (!smsResponse.isSuccess()){
            VerifyCode update = new VerifyCode();
            update.setId(verifyCode.getId());
            update.setStatus(VerifyCodeStatusEnum.INVALID.name());
            verifyCodeService.updateByPrimaryKeySelective(update);
            throw new BusinessException(SmsErrorCodeEnum.SEND_SMS_ERROR.getCode(),SmsErrorCodeEnum.SEND_SMS_ERROR.getMsg());
        }

    }


    /**
     * 校验短信验证码
     *
     * @param param
     */
    public void validateSmsCode(ValidateSmsCodeParam param){
        //获取当前用户信息
        //userId等可以直接在userContext中获取
        UserInfo userInfo = UserContext.get();

        VerifyCode verifyCode = verifyCodeService.selectValidCode(userInfo.getUserId(), param.getOriginMobile(), param.getActionType());
        //校验验证码是否存在
        if (verifyCode == null){
            throw new BusinessException(VerifyCodeErrorCodeEnum.SMS_CODE_NOT_EXISTS.getCode(),VerifyCodeErrorCodeEnum.SMS_CODE_NOT_EXISTS.getMsg());
        }
        //校验验证码是否过期
        if (verifyCode.getExpireTime().getTime() < System.currentTimeMillis()){
            throw new BusinessException(VerifyCodeErrorCodeEnum.SMS_CODE_EXPIRE.getCode(),VerifyCodeErrorCodeEnum.SMS_CODE_EXPIRE.getMsg());
        }
        //校验验证码是否正确
        if (!verifyCode.getCode().equals(param.getCode()) ){
            //允许两次输入验证码错误
            if (verifyCode.getFailCount() < 2){
                //失败次数加一，状态改为验证错误
                updateVerifyCodeStatusWrong(verifyCode);

            } else {
                //超过错误次数，将状态改为FAIL
                updateVerifyCodeStatusFail(verifyCode);
            }
        }
        //更改验证码状态为成功
        updateVerifyCodeStatusSuccess(verifyCode);

    }


    //失败次数加一，状态改为验证错误
    private void updateVerifyCodeStatusWrong(VerifyCode verifyCode) {
        VerifyCode update = new VerifyCode();
        update.setId(verifyCode.getId());
        update.setFailCount(verifyCode.getFailCount()+ 1);
        update.setStatus(VerifyCodeStatusEnum.WRONG.name());
        verifyCodeService.updateByPrimaryKeySelective(update);
        throw new BusinessException(VerifyCodeErrorCodeEnum.SMS_CODE_WRONG.getCode(),VerifyCodeErrorCodeEnum.SMS_CODE_WRONG.getMsg());

    }


    //将更改验证码状态为成功
    private void updateVerifyCodeStatusSuccess(VerifyCode verifyCode) {
        VerifyCode update = new VerifyCode();
        update.setId(verifyCode.getId());
        update.setStatus(VerifyCodeStatusEnum.SUCCESS.name());
        verifyCodeService.updateByPrimaryKeySelective(update);
    }

    //将状态改为FAIL
    private void updateVerifyCodeStatusFail(VerifyCode verifyCode) {
        VerifyCode update = new VerifyCode();
        update.setId(verifyCode.getId());
        update.setStatus(VerifyCodeStatusEnum.FAIL.name());
        verifyCodeService.updateByPrimaryKeySelective(update);
        throw new BusinessException(VerifyCodeErrorCodeEnum.SMS_CODE_FAIL.getCode(),VerifyCodeErrorCodeEnum.SMS_CODE_FAIL.getMsg());

    }

    /**
     * 保存生成验证码的记录
     *
     * @return
     */
    private VerifyCode saveVerifyCode(Long userId, String mobile, String code, SmsActionType actionType) {
        //批量更改，一般不用
        //先将过期的code的status改为INVALID
        verifyCodeService.updateExpireCode(userId, mobile, actionType);

        //插入新的
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setUserId(userId);
        verifyCode.setMobile(mobile);
        verifyCode.setCode(code);
        verifyCode.setActionType(actionType.name());
        verifyCode.setFailCount(0);
        verifyCode.setExpireTime(generateSmsCodeExpireTime());
        verifyCodeService.insertSelective(verifyCode);

        return verifyCode;
    }


    /**
     * 生成验证码过期时间
     * @return
     */
    private Date generateSmsCodeExpireTime() {

        return new Date((System.currentTimeMillis() + SMS_CODE_EXPIER_TIME));

    }





    /**
     * 生成短信验证码
     * @return
     */
    private String generateSmsCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < SMS_CODE_LENGTH; i++) {
            code.append(RandomUtils.nextInt(0,10));
        }
        return code.toString();
    }
}
