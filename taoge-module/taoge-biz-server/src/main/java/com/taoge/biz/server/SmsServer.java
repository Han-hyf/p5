package com.taoge.biz.server;

import com.alibaba.fastjson.JSON;
import com.taoge.biz.common.constant.SmsConstant;
import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.common.errorCode.SmsErrorCodeEnum;
import com.taoge.biz.common.redis.SmsRedisKey;
import com.taoge.biz.persistent.entity.SmsRecord;
import com.taoge.biz.persistent.service.SmsRecordService;
import com.taoge.biz.server.param.sms.SendSmsCodeParam;
import com.taoge.biz.server.vo.sms.SmsResponse;
import com.taoge.framework.exception.BusinessException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServer {
    @Resource
    SmsRecordService smsRecordService;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    private static final String secretId = "AKIDd5JJ5cw75pLekcLplV1Mbdw3wa0DCakg";
    private static final String secretKey = "rUr1ygDWat0tYgceS6XllWBgy3we8Nfu";
    private static final String sdkAppId = "1400456063";
    private static final String signName = "优旷科技";

    /**
     * 每天可以发送短信的总条数
     */
    private static final int SMS_MAX_COUNT = 999;

    private static final List<String> smsIsoWhiteList;

    static {
        smsIsoWhiteList = new ArrayList<>();
        smsIsoWhiteList.add("CN");
    }

    public SmsResponse sendCodeSms(SendSmsCodeParam param, String code) {

        // 保存短信发送记录
        SmsRecord smsRecord = saveSmsRecord(param.getUserId(), param.getOriginMobile(), param.getMobilePrefix(), param.getIso(), param.getActionType(), param.getIp());

        // 调用发送短信服务，可以对接阿里云、腾讯云、金山云等云平台短信sdk
        SmsResponse smsResponse = sendTxSmsCode(smsRecord.getMobile(), param.getActionType(), code);

        // 记录发送短信条数
        incrementSendSmsCount(param);

        // 根据发送结果，更新短信记录状态
        updateSmsRecordBySmsResponse(smsResponse, smsRecord.getId());

        return smsResponse;
    }

    /**
     * 根据发送结果，更新短信记录状态
     *
     * @param smsResponse 短信发送结果
     * @param smsRecordId 短信记录id
     */
    private void updateSmsRecordBySmsResponse(SmsResponse smsResponse, Long smsRecordId) {
        SmsRecord update = new SmsRecord();
        update.setId(smsRecordId);
        update.setTemplateCode(smsResponse.getTemplateId());
        update.setTemplateParam(JSON.toJSONString(smsResponse.getTemplateParam()));
        update.setSendMessage(smsResponse.getSendMessage());
        if (smsResponse.isSuccess()) {
            update.setSendStatus(true);
        } else {
            update.setSendStatus(false);
        }
        smsRecordService.updateByPrimaryKeySelective(update);
    }

    /**
     * 发送腾讯短信
     *
     * @return
     */
    public SmsResponse sendTxSmsCode(String mobile, SmsActionType actionType, String code) {
        try {
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-beijing", clientProfile);
            SendSmsRequest req = new SendSmsRequest();

            req.setSmsSdkAppid(sdkAppId);
            req.setSign(signName);

            // 根据业务获取短信模板id
            String templateId = getTemplateId(actionType);
            // 获取短信模板参数
            String[] templateParam = SmsConstant.generateTxTemplateParam(actionType, code);

            req.setTemplateID(templateId);
            req.setTemplateParamSet(templateParam);

            String[] phoneNumberSet = {mobile};
            req.setPhoneNumberSet(phoneNumberSet);

            // 定义发送短信返回结果
            SmsResponse smsResponse = new SmsResponse();
            smsResponse.setTemplateId(templateId);
            smsResponse.setTemplateParam(templateParam);

            // 判断如果是开发环境，就不发送真实短信，默认返回成功
            if (SmsConstant.isEnvDev()) {
                smsResponse.setSuccess(true);
            } else {
                SendSmsResponse res = client.SendSms(req);
                // 判断是否发送成功
                smsResponse.setSuccess("Ok".equals(res.getSendStatusSet()[0].getCode()));
                smsResponse.setSendMessage(JSON.toJSONString(res.getSendStatusSet()[0]));
                log.info("sendTxSmsCode SendSmsResponse:{}", SendSmsResponse.toJsonString(res));
            }
            return smsResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存短信发送记录
     */
    public SmsRecord saveSmsRecord(Long userId, String originMobile, String mobilePrefix, String iso, SmsActionType actionType, String ip) {
        SmsRecord smsRecord = new SmsRecord();
        smsRecord.setUserId(userId);
        smsRecord.setMobile(mobilePrefix + originMobile);
        smsRecord.setMobilePrefix(mobilePrefix);
        smsRecord.setIso(iso);
        smsRecord.setOriginalMobile(originMobile);
        smsRecord.setActionType(actionType.name());
        smsRecord.setIp(ip);
        smsRecord.setSendTime(new Date());
        smsRecord.setSendDay(new Date());
        smsRecordService.insertSelective(smsRecord);
        return smsRecord;
    }

    /**
     * 校验短信信息
     */
    public void validateSmsInfo(SendSmsCodeParam param) {
        // 校验国家
        if (!smsIsoWhiteList.contains(param.getIso())) {
            throw new BusinessException(SmsErrorCodeEnum.SEND_SMS_ISO_ERROR.getCode(), SmsErrorCodeEnum.SEND_SMS_ISO_ERROR.getMsg());
        }
        // 校验当天，userId维度发送条数是否超上限
        int smsUserIdCount = countByIdentityInDay(param.getUserId().toString(), param.getActionType());
        if (smsUserIdCount >= param.getActionType().getMaxCountByDay()) {
            throw new BusinessException(SmsErrorCodeEnum.SEND_SMS_DAY_MAX_COUNT_ERROR.getCode(), SmsErrorCodeEnum.SEND_SMS_DAY_MAX_COUNT_ERROR.getMsg());
        }

        // 校验当天，手机号维度发送条数是否超上限
        int smsMobileCount = countByIdentityInDay(param.getMobilePrefix() + param.getOriginMobile(), param.getActionType());
        if (smsMobileCount >= param.getActionType().getMaxCountByDay()) {
            throw new BusinessException(SmsErrorCodeEnum.SEND_SMS_DAY_MAX_COUNT_ERROR.getCode(), SmsErrorCodeEnum.SEND_SMS_DAY_MAX_COUNT_ERROR.getMsg());
        }

        // 校验当天，ip维度发送条数是否超上限
        int smsIpCount = countByIdentityInDay(param.getIp(), param.getActionType());
        if (smsIpCount >= param.getActionType().getMaxCountByDay()) {
            throw new BusinessException(SmsErrorCodeEnum.SEND_SMS_DAY_MAX_COUNT_ERROR.getCode(), SmsErrorCodeEnum.SEND_SMS_DAY_MAX_COUNT_ERROR.getMsg());
        }

        // 当天系统的短信发送总量
        int smsTotal = countInDay();
        if (smsTotal >= SMS_MAX_COUNT) {
            throw new BusinessException(SmsErrorCodeEnum.SEND_SMS_DAY_MAX_COUNT_ERROR.getCode(), SmsErrorCodeEnum.SEND_SMS_DAY_MAX_COUNT_ERROR.getMsg());
        }

    }

    /**
     * 按标识查询当天发送总条数
     *
     * @param identity   标识：userId,mobile,ip
     * @param actionType 业务类型
     */
    public int countByIdentityInDay(String identity, SmsActionType actionType) {
        // redis key
        String count = stringRedisTemplate.opsForValue().get(SmsRedisKey.getSmsCountInDayKey(identity, actionType, DateFormatUtils.format(new Date(), "yyyyMMdd")));
        if (null != count) {
            return Integer.parseInt(count);
        }
        return 0;
    }

    /**
     * 查询当天发送总量
     */
    public int countInDay() {
        // redis key
        String count = stringRedisTemplate.opsForValue().get(SmsRedisKey.getSmsTotalInDay(DateFormatUtils.format(new Date(), "yyyyMMdd")));
        if (null != count) {
            return Integer.parseInt(count);
        }
        return 0;
    }

    /**
     * 记录已发送条数
     */
    private void incrementSendSmsCount(SendSmsCodeParam param) {
        String day = DateFormatUtils.format(new Date(), "yyyyMMdd");

        // 批量执行redis命令
        stringRedisTemplate.setEnableTransactionSupport(true);
        stringRedisTemplate.multi();
        try {
            stringRedisTemplate.opsForValue().increment(SmsRedisKey.getSmsCountInDayKey(param.getUserId().toString(), param.getActionType(), day));
            stringRedisTemplate.expire(SmsRedisKey.getSmsCountInDayKey(param.getUserId().toString(), param.getActionType(), day), 1, TimeUnit.DAYS);

            stringRedisTemplate.opsForValue().increment(SmsRedisKey.getSmsCountInDayKey(param.getMobilePrefix() + param.getOriginMobile(), param.getActionType(), day));
            stringRedisTemplate.expire(SmsRedisKey.getSmsCountInDayKey(param.getMobilePrefix() + param.getOriginMobile(), param.getActionType(), day), 1, TimeUnit.DAYS);

            stringRedisTemplate.opsForValue().increment(SmsRedisKey.getSmsCountInDayKey(param.getIp(), param.getActionType(), day));
            stringRedisTemplate.expire(SmsRedisKey.getSmsCountInDayKey(param.getIp(), param.getActionType(), day), 1, TimeUnit.DAYS);

            stringRedisTemplate.opsForValue().increment(SmsRedisKey.getSmsTotalInDay(day));
            stringRedisTemplate.expire(SmsRedisKey.getSmsTotalInDay(day), 1, TimeUnit.DAYS);
            stringRedisTemplate.exec();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据业务类型获取模板id
     *
     * @param actionType 业务类型
     */
    private String getTemplateId(SmsActionType actionType) {
        return SmsConstant.getSmsTemplateCode(actionType);
    }


}
