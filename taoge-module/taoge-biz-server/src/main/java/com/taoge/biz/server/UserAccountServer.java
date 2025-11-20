package com.taoge.biz.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.taoge.biz.common.enums.UserVipRecordStatusEnum;
import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.common.enums.BusinessTypeEnum;
import com.taoge.biz.common.errorCode.OrderErrorCodeEnum;
import com.taoge.biz.common.errorCode.UserErrorCodeEnum;
import com.taoge.biz.common.param.OrderSnParam;
import com.taoge.biz.common.redis.UserVipConfigRedisKey;
import com.taoge.biz.persistent.entity.BusinessOrder;
import com.taoge.biz.persistent.entity.UserVipConfig;
import com.taoge.biz.persistent.entity.UserVipRecord;
import com.taoge.biz.persistent.service.UserVipConfigService;
import com.taoge.biz.persistent.service.UserVipRecordService;
import com.taoge.biz.persistent.service.UserVipService;
import com.taoge.biz.server.param.order.ApplyBusinessOrderParam;
import com.taoge.biz.server.param.order.PayOrderParam;
import com.taoge.biz.server.param.vip.*;
import com.taoge.biz.server.vo.order.ApplyAliOrderVO;
import com.taoge.biz.server.vo.order.ApplyWxOrderVO;
import com.taoge.biz.server.vo.vip.ApplyBuyVipVO;
import com.taoge.biz.server.vo.vip.ApplyPayBuyVipVO;
import com.taoge.biz.server.vo.vip.UserVipConfigVO;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.exception.BusinessException;
import com.taoge.framework.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;



@Service
@Slf4j
public class UserAccountServer {

    @Resource
    UserVipConfigService userVipConfigService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    BusinessOrderServer businessOrderServer;
    @Resource
    UserVipRecordService userVipRecordService;
    @Resource
    UserVipService userVipService;


    /**
     * 添加会员配置
     */
    public void addUserVipConfig(AddUserVipConfigParam param){

        userVipConfigService.add(param.getVipName(), param.getVipPrice(), param.getVipIcon(),
                param.getVipDays(), param.getVipDaysName(), param.getVipLevel());
        //刷新缓存
        refreshUserVipConfigListByRedis();
    }

    /**
     * 修改会员配置
     */
    public void updateUserVipConfig(UpdateUserVipConfigParam param){
        userVipConfigService.update(param.getId(), param.getVipName(), param.getVipPrice(),
                param.getVipIcon(), param.getVipDays(), param.getVipDaysName(), param.getVipLevel());

        //刷新缓存
        refreshUserVipConfigListByRedis();
    }

    /**
     * 查询会员配置列表
     */
    public List<UserVipConfigVO> userVipConfigList(UserVipConfigListParam param){
        //查询redis
        List<UserVipConfigVO> volist = getUserVipConfigListByRedis();
        if (CollectionUtils.isEmpty(volist)){
            return volist;
        }

        //redis没有后,查数据库
        volist = userVipConfigService.voList(param, UserVipConfigVO.class);
        //将数据存入redis
        setUserVipConfigListByRedis(volist);
        return volist;
    }

    /**
     * 启用会员配置
     */
    public void enableUserVipConfig(IdParam param){
        userVipConfigService.updateStatus(param.getId(), true);
    }

    /**
     * 禁用会员配置
     */
    public void disableUserVipConfig(IdParam param){
        userVipConfigService.updateStatus(param.getId(), false);

    }

    /**
     * 会员配置排序
     */
    public void sortVipConfig(List<Long> ids){
        //根据id,排序
        userVipConfigService.sort(ids);
    }


    /**
     * 从redis查询会员配置
     */
    public List<UserVipConfigVO> getUserVipConfigListByRedis( ){
        //获得key
        String key = UserVipConfigRedisKey.getUserVipConfigListKey();

        //根据key查询数据
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s == null){
            return null;
        }
        //将String转为JSON
        List<UserVipConfigVO> voList = JSONArray.parseArray(s, UserVipConfigVO.class);
        return voList;
    }

    /**
     * 将会员数据存入redis
     */
    public void setUserVipConfigListByRedis(List<UserVipConfigVO> list){
        String key = UserVipConfigRedisKey.getUserVipConfigListKey();
        String s = "";
        if (CollectionUtils.isEmpty(list)){
            //将JSON转为String
            s = JSON.toJSONString(list);
        }
        stringRedisTemplate.opsForValue().set(key,s,10, TimeUnit.SECONDS);
    }


    /**
     * 刷新redis的会员列表
     */
    public void refreshUserVipConfigListByRedis(){
        String key = UserVipConfigRedisKey.getUserVipConfigListKey();
        stringRedisTemplate.delete(key);
    }

    /**
     * 创建购买vip订单
     */
    @Transactional
    public ResponseData<ApplyBuyVipVO> applyBuyVip(ApplyBuyVipParam param){

        //查看当前购买的VIP的状态对不对
        UserVipConfig userVipConfig = userVipConfigService.get(param.getUserVipConfigId());
        if (userVipConfig == null || !userVipConfig.getStatus()){
            throw new BusinessException(UserErrorCodeEnum.VIP_CONFIG_NOT_EXIST.getCode(),UserErrorCodeEnum.VIP_CONFIG_NOT_EXIST.getMsg());

        }

        ResponseData<ApplyBuyVipVO> responseData = (ResponseData<ApplyBuyVipVO>) ResponseData.success();
        UserInfo userInfo = UserContext.get();
        ApplyBusinessOrderParam applyBusinessOrderParam = new ApplyBusinessOrderParam();
        applyBusinessOrderParam.setUserId(userInfo.getUserId());
        applyBusinessOrderParam.setBusinessType(BusinessTypeEnum.BUY_VIP);
        applyBusinessOrderParam.setBusinessParam(userVipConfig.getId().toString());
        applyBusinessOrderParam.setTotalMoney(userVipConfig.getVipPrice());
        applyBusinessOrderParam.setPayMoney(userVipConfig.getVipPrice());

        //  生成业务订单
        BusinessOrder businessOrder = null;
        try {
            businessOrder = businessOrderServer.applyBusinessOrder(applyBusinessOrderParam);
        } catch (BusinessException e) {
            //抛异常说明订单已存在
            if (e.getCode() == OrderErrorCodeEnum.INIT_EXIST_ERROR.getCode()){
                responseData.setCode(e.getCode());
                responseData.setMsg(e.getMessage());
                businessOrder = (BusinessOrder) e.getData();
                ApplyBuyVipVO vo = new ApplyBuyVipVO();
                vo.setBusinessOrderSn(businessOrder.getBusinessOrderSn());
                responseData.setData(vo);
                return responseData;
            }
        }
        if (null == businessOrder){
            throw new BusinessException(OrderErrorCodeEnum.APPLY_ORDER_ERROR.getCode(),OrderErrorCodeEnum.APPLY_ORDER_ERROR.getMsg());
        }

        //  生成购买会员订单
        userVipRecordService.appBuyVip(userInfo.getUserId(),businessOrder.getBusinessOrderSn(),userVipConfig.getVipName(),
                userVipConfig.getVipPrice(),userVipConfig.getVipIcon(),userVipConfig.getVipDays(),userVipConfig.getVipDaysName(),
                userVipConfig.getVipLevel(),businessOrder.getPayMoney());

        //  返回订单编号
        ApplyBuyVipVO vo = new ApplyBuyVipVO();
        vo.setBusinessOrderSn(businessOrder.getBusinessOrderSn());
        responseData.setData(vo);
        return responseData;

    }


    /**
     * 创建支付订单
     */
    public ApplyPayBuyVipVO applyPayBuyVip(ApplyPayBuyVipParam param){

        ApplyPayBuyVipVO vo = new ApplyPayBuyVipVO();
        PayOrderParam payOrderParam = new PayOrderParam();
        payOrderParam.setBusinessOrderSn(param.getBusinessOrderSn());
        //按支付类型创建支付订单
        switch (param.getPayType()){
            case WX_PAY:
                ApplyWxOrderVO applyWxOrderVO = businessOrderServer.applyWxPay(payOrderParam);
                vo.setApplyWxOrderVO(applyWxOrderVO);
                vo.setPayTypeEnum(param.getPayType());
                break;
            case ALI_PAY:
                ApplyAliOrderVO applyAliOrderVO = businessOrderServer.applyAliPay(payOrderParam);
                vo.setApplyAliOrderVO(applyAliOrderVO);
                break;
        }

        return vo;
    }


    /**
     * 购买VIP成功
     * @param param
     */
    public void buyVipSuccess(OrderSnParam param){

        //校验购买记录状态,如果不是INIT,就结束
        UserVipRecord userVipRecord = userVipRecordService.getByBusinessOrderSn(param.getBusinessOrderSn());
        if (userVipRecord == null){
            log.error("buyVipSuccess error, userVipRecord not exist ,businessOrderSn:{}",param.getBusinessOrderSn());
            return;
        }

        if (userVipRecord.getStatus().equals(UserVipRecordStatusEnum.INIT)){
            log.error("buyVipSuccess error, userVipRecord status not INIT,businessOrderSn:{} status:{}",param.getBusinessOrderSn(),userVipRecord.getStatus());
            return;
        }

        // 更新会员购买记录,在 UserVipRecordService 中
        if (!userVipRecordService.paySuccess(param.getBusinessOrderSn())){
            //如果没有更新成功,发送飞书预警
            log.error("businessOrder paySuccess error, businessOrderSn:{}",param.getBusinessOrderSn());
            return;
        }

        //激活用户会员
        userVipService.activateUserVip(userVipRecord);

    }

}
