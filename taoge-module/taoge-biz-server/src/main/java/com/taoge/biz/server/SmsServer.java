package com.taoge.biz.server;

import com.taoge.biz.common.constant.SmsConstant;
import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.common.errorCode.SmsErrorCodeEnum;
import com.taoge.biz.common.redis.SmsRedisKey;
import com.taoge.biz.persistent.entity.SmsRecord;
import com.taoge.biz.persistent.service.SmsRecordService;
import com.taoge.biz.server.param.sms.SendSmsCodeParam;
import com.taoge.biz.server.vo.sms.SmsResponse;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.exception.BusinessException;
import com.taoge.framework.util.UserContext;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.taoge.biz.common.redis.SmsRedisKey.getSmsCountInDayKey;
import static com.taoge.biz.common.redis.SmsRedisKey.getSmsTotalInDay;

@Service
public class SmsServer {
    @Resource
    SmsRecordService smsRecordService;
    @Resource
    StringRedisTemplate stringRedisTemplate;


    /**
     * 所有业务每天可以发送短信的总量
     */
    private static final int SMS_MAX_CODE = 4;



    public SmsResponse sendCodeSms(SendSmsCodeParam param, String code){
        //保存短信发送记录
        SmsRecord smsRecord = saveSmsRecode(param.getUserId(), param.getOriginMobile(), param.getMobilePrefix(), param.getIso(), param.getActionType(), param.getTemplateParam(), param.getIp());
        //调用短信发送服务
        SmsResponse smsResponse = sendTxSmsCode(param.getActionType(), param.getTemplateParam());
        //记录已发送的条数
        incrementSendSmsCount(param);
        // 根据发送的结果，更新短信记录状态
        updateSmsRecordByResponse(smsResponse,smsRecord.getId());
        return smsResponse;

    }

    /**
     * 根据发送结果，更新短信状态
     * @param smsResponse
     * @param smsRecordId
     */
    private void updateSmsRecordByResponse(SmsResponse smsResponse,Long smsRecordId) {
        SmsRecord update = new SmsRecord();
        update.setId(smsRecordId);
        update.setTemplateCode(smsResponse.getTemplateId());

        if (smsResponse.isSuccess()){
            update.setSendStatus(true);
        }else {
            update.setSendStatus(false);
        }
        smsRecordService.updateByPrimaryKeySelective(update);

    }


    public SmsResponse sendTxSmsCode(SmsActionType actionType,String templateParam){

        //根据模板类型找到模板id
        String templateId = getTemplateId(actionType);
        System.out.println(String.format("已发送验证码,模板为%s",templateId));
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setTemplateId(templateId);
        smsResponse.setTemplateParam(templateParam);
        smsResponse.setSuccess(true);
        smsResponse.setChannel("腾讯或阿里等不同平台");
        return smsResponse;
    }


    /**
     * 根据模板获得对应短信id
     * @param actionType
     * @return
     */
    private String getTemplateId(SmsActionType actionType) {

        return SmsConstant.getSmsTemplateCode(actionType);
    }

    /**
     * //保存短信发送记录
     */
    public SmsRecord saveSmsRecode(Long userId, String originMobile, String mobilePrefix, String iso, SmsActionType actionType,  String templateParam, String ip){
        SmsRecord smsRecord = new SmsRecord();

        smsRecord.setUserId(userId);
        smsRecord.setMobile(mobilePrefix+originMobile);
        smsRecord.setMobilePrefix(mobilePrefix);
        smsRecord.setIso(iso);
        smsRecord.setOriginalMobile(originMobile);
        //actionType.name()相当于toString,将枚举打印出来
        smsRecord.setActionType(actionType.name());
        //smsRecord.setContent();
        //smsRecord.setTemplateCode(templateCode);
        smsRecord.setTemplateParam(templateParam);
        smsRecord.setIp(ip);
        //smsRecord.setSendMessage();
        smsRecord.setSendTime(new Date());
        smsRecord.setSendDay(new Date());


        smsRecordService.insertSelective(smsRecord);

        return smsRecord;
    }

    public void validateSmsInfo(SendSmsCodeParam param){
        //校验国家
        if(!"+86".equals(param.getMobilePrefix())){
            throw new BusinessException(SmsErrorCodeEnum.SEND_SMS_ISO_ERROR.getCode(),SmsErrorCodeEnum.SEND_SMS_ISO_ERROR.getMsg());
        }

        //按用户ID
        int smsUserIDCount = countByIdentityInDay(param.getUserId().toString(),param.getActionType());
        if (smsUserIDCount >= param.getActionType().getMaxCountByDay()){
            throw new BusinessException(SmsErrorCodeEnum.SEND_SMS_MAX_COUNT.getCode(),
                    SmsErrorCodeEnum.SEND_SMS_MAX_COUNT.getMsg());
        }

        //按手机号
        int smsMobileCount = countByIdentityInDay(param.getMobilePrefix()+param.getOriginMobile(),param.getActionType());
        if (smsMobileCount >= param.getActionType().getMaxCountByDay()){
            throw new BusinessException(SmsErrorCodeEnum.SEND_SMS_MAX_COUNT.getCode(),
                    SmsErrorCodeEnum.SEND_SMS_MAX_COUNT.getMsg());
        }

        //按IP
        int smsIpCount = countByIdentityInDay(param.getIp(),param.getActionType());
        if (smsIpCount >= param.getActionType().getMaxCountByDay()){
            throw new BusinessException(SmsErrorCodeEnum.SEND_SMS_MAX_COUNT.getCode(),
                    SmsErrorCodeEnum.SEND_SMS_MAX_COUNT.getMsg());
        }

        //
        int smsTotal = countInDay();
        if (smsTotal > SMS_MAX_CODE){
            throw new BusinessException(SmsErrorCodeEnum.SEND_SMS_MAX_COUNT.getCode(),
                    SmsErrorCodeEnum.SEND_SMS_MAX_COUNT.getMsg()+SMS_MAX_CODE+"条");
        }



    }

    /**
     * 按条件查询当天发送的短信的次数
     * @param identity 标识：userId,mobile,ip
     * @param actionType
     * @return
     */
    public int countByIdentityInDay(String identity,SmsActionType actionType){
        String day = DateFormatUtils.format(new Date(),"yyyyMMdd");
        String key = getSmsCountInDayKey(identity,actionType,day);
        String count = stringRedisTemplate.opsForValue().get(key);
        if(count != null){
            return Integer.parseInt(count);
        }
        return 0;
    }


    /**
     * 查询当天发送总量
     * @return
     */
    public int countInDay(){
        String day = DateFormatUtils.format(new Date(),"yyyyMMdd");
        String key = getSmsTotalInDay(day);
        String count = stringRedisTemplate.opsForValue().get(key);
        if(count != null){
            return Integer.parseInt(count);
        }
        return 0;
    }


    /**
     * 记录已发送的条数
     * @param param
     */
    private void incrementSendSmsCount(SendSmsCodeParam param) {
        String day = DateFormatUtils.format(new Date(),"yyyyMMdd");

        //批量执行redis命令
        stringRedisTemplate.setEnableTransactionSupport(true);
        stringRedisTemplate.multi();
        try {
            //按userId的
            stringRedisTemplate.opsForValue().increment(SmsRedisKey.getSmsCountInDayKey(param.getUserId().toString(), param.getActionType(),day));
            stringRedisTemplate.expire(SmsRedisKey.getSmsCountInDayKey(param.getUserId().toString(), param.getActionType(),day),1, TimeUnit.DAYS);
            //按手机号的
            stringRedisTemplate.opsForValue().increment(SmsRedisKey.getSmsCountInDayKey(param.getMobilePrefix()+param.getOriginMobile(), param.getActionType(),day));
            stringRedisTemplate.expire(SmsRedisKey.getSmsCountInDayKey(param.getMobilePrefix()+param.getOriginMobile(), param.getActionType(),day),1, TimeUnit.DAYS);
            //按Ip的
            stringRedisTemplate.opsForValue().increment(SmsRedisKey.getSmsCountInDayKey(param.getIp(), param.getActionType(),day));
            stringRedisTemplate.expire(SmsRedisKey.getSmsCountInDayKey(param.getIp(), param.getActionType(),day),1, TimeUnit.DAYS);
            //总的
            stringRedisTemplate.opsForValue().increment(SmsRedisKey.getSmsTotalInDay(day));
            stringRedisTemplate.expire(SmsRedisKey.getSmsTotalInDay(day),1, TimeUnit.DAYS);

            stringRedisTemplate.exec();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
