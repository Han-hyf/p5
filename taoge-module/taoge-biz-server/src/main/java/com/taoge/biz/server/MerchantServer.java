package com.taoge.biz.server;

import com.taoge.biz.common.enums.MerchantShopAuditStatusEnum;
import com.taoge.biz.common.enums.MerchantShopStatusEnum;
import com.taoge.biz.common.errorCode.MerchantErrorCodeEnum;
import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.common.util.PasswordUtil;
import com.taoge.biz.persistent.entity.Merchant;
import com.taoge.biz.persistent.entity.MerchantAccount;
import com.taoge.biz.persistent.entity.MerchantShop;
import com.taoge.biz.persistent.entity.MerchantShopAudit;
import com.taoge.biz.persistent.service.MerchantAccountService;
import com.taoge.biz.persistent.service.MerchantService;
import com.taoge.biz.persistent.service.MerchantShopAuditService;
import com.taoge.biz.persistent.service.MerchantShopService;
import com.taoge.biz.server.param.merchant.CreateShopParam;
import com.taoge.biz.server.param.merchant.EditShopParam;
import com.taoge.biz.server.param.merchant.LoginByUsernameParam;
import com.taoge.biz.server.param.merchant.RegisterMerchantParam;
import com.taoge.biz.server.vo.merchant.MerchantLoginVO;
import com.taoge.biz.server.vo.merchant.MerchantShopVO;
import com.taoge.framework.common.MerchantInfo;
import com.taoge.framework.controller.BaseVO;
import com.taoge.framework.exception.BusinessException;
import com.taoge.framework.util.MerchantContext;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class MerchantServer {
    @Resource
    MerchantService merchantService;
    @Resource
    MerchantAccountService merchantAccountService;
    @Resource
    MerchantShopService merchantShopService;
    @Resource
    MerchantShopAuditService merchantShopAuditService;

    /**
     * 注册商户
     */
    @Transactional
    public void registerMerchant(RegisterMerchantParam param) {
        // 校验手机号是否注册
        Merchant exists = merchantService.getByMobile(param.getMobile());
        if (null != exists) {
            throw new BusinessException(MerchantErrorCodeEnum.MOBILE_EXISTS_ERROR.getCode(), MerchantErrorCodeEnum.MOBILE_EXISTS_ERROR.getMsg());
        }
        // 校验用户名是否注册
        exists = merchantService.getByUsername(param.getUsername());
        if (null != exists) {
            throw new BusinessException(MerchantErrorCodeEnum.USERNAME_EXISTS_ERROR.getCode(), MerchantErrorCodeEnum.USERNAME_EXISTS_ERROR.getMsg());
        }
        // 校验商户名是否注册
        exists = merchantService.getByMerchantName(param.getMerchantName());
        if (null != exists) {
            throw new BusinessException(MerchantErrorCodeEnum.MERCHANT_NAME_EXISTS_ERROR.getCode(), MerchantErrorCodeEnum.MERCHANT_NAME_EXISTS_ERROR.getMsg());
        }
        // 注册商户
        Merchant merchant = param.convertTo(Merchant.class);
        // 生成salt方法
        String salt = PasswordUtil.generateSalt();
        // 密码需要加密，抽取加密方法 md5(password + salt)
        merchant.setPassword(PasswordUtil.encryptionPassword(param.getPassword(), salt));
        merchant.setSalt(salt);
        merchantService.insertSelective(merchant);

        // 初始化账户
        MerchantAccount merchantAccount = new MerchantAccount();
        merchantAccount.setMerchantId(merchant.getId());
        merchantAccountService.insertSelective(merchantAccount);

        // TODO 发送注册商户信息，处理统计数据
    }

    /**
     * 账号密码登录
     */
    public MerchantLoginVO loginByUsername(LoginByUsernameParam param) {
        // 校验账号是否存在
        Merchant merchant = merchantService.getByUsername(param.getUsername());
        if (null == merchant) {
            throw new BusinessException(MerchantErrorCodeEnum.USERNAME_LOGIN_ERROR.getCode(), MerchantErrorCodeEnum.USERNAME_LOGIN_ERROR.getMsg());
        }
        // 校验密码是否正确
        String password = PasswordUtil.encryptionPassword(param.getPassword(), merchant.getSalt());
        if (!password.equals(merchant.getPassword())) {
            throw new BusinessException(MerchantErrorCodeEnum.USERNAME_LOGIN_ERROR.getCode(), MerchantErrorCodeEnum.USERNAME_LOGIN_ERROR.getMsg());
        }
        // 登录成功
        return loginSuccess(merchant);
    }

    private MerchantLoginVO loginSuccess(Merchant merchant) {
        MerchantLoginVO vo = new MerchantLoginVO();
        vo.setMerchantId(merchant.getId());
        vo.setMerchantName(merchant.getMerchantName());
        vo.setMerchantLogo(merchant.getMerchantLogo());
        return vo;
    }

    /**
     * 创建店铺
     */
    @Transactional
    public void createShop(CreateShopParam param) {
        // 校验是否已注册店铺
        MerchantInfo merchantInfo = MerchantContext.get();
        MerchantShop merchantShop = merchantShopService.getByMerchantId(merchantInfo.getUserId());
        if (null != merchantShop) {
            throw new BusinessException(MerchantErrorCodeEnum.SHOP_EXISTS_ERROR.getCode(), MerchantErrorCodeEnum.SHOP_EXISTS_ERROR.getMsg());
        }
        // 创建待审核店铺
        merchantShop = param.convertTo(MerchantShop.class);
        merchantShop.setMerchantId(merchantInfo.getUserId());
        merchantShop.setStatus(MerchantShopStatusEnum.WAIT_AUDIT.name());
        merchantShopService.insertSelective(merchantShop);

        // 添加待审核记录
        MerchantShopAudit waitAudit = param.convertTo(MerchantShopAudit.class);
        waitAudit.setMerchantId(merchantInfo.getUserId());
        waitAudit.setShopId(merchantShop.getId());
        waitAudit.setAuditStatus(MerchantShopAuditStatusEnum.WAIT.name());
        merchantShopAuditService.insertSelective(waitAudit);

    }

    /**
     * 编辑店铺
     */
    @Transactional
    public void editShop(EditShopParam param) {
        // 校验店铺是否存在
        MerchantShop exists = merchantShopService.get(param.getId());
        if (null == exists) {
            throw new BusinessException(MerchantErrorCodeEnum.SHOP_NOT_EXISTS_ERROR.getCode(), MerchantErrorCodeEnum.SHOP_NOT_EXISTS_ERROR.getMsg());
        }
        // 获取 merchantId
        MerchantInfo merchantInfo = MerchantContext.get();
        if (exists.getMerchantId().longValue() != merchantInfo.getUserId()) {
            throw new BusinessException(MerchantErrorCodeEnum.SHOP_NOT_EXISTS_ERROR.getCode(), MerchantErrorCodeEnum.SHOP_NOT_EXISTS_ERROR.getMsg());
        }
        // 校验是否有待审核记录
        HashMap<String, Object> auditParam = new HashMap<>();
        auditParam.put("auditStatus", MerchantShopAuditStatusEnum.WAIT.name());
        auditParam.put("merchantId", merchantInfo.getUserId());
        List<MerchantShopAudit> auditList = merchantShopAuditService.list(auditParam);
        if (CollectionUtils.isNotEmpty(auditList)) {
            throw new BusinessException(MerchantErrorCodeEnum.SHOP_WAIT_AUDIT_EXISTS_ERROR.getCode(), MerchantErrorCodeEnum.SHOP_WAIT_AUDIT_EXISTS_ERROR.getMsg());
        }

        // 添加待审核记录
        MerchantShopAudit merchantShopAudit = param.convertTo(MerchantShopAudit.class);
        merchantShopAudit.setMerchantId(merchantInfo.getUserId());
        merchantShopAudit.setShopId(exists.getId());

        merchantShopAuditService.insertSelective(merchantShopAudit);
    }

    /**
     * 查询当前用户店铺信息，提供给cms项目
     */
    public MerchantShopVO getMyShop() {
        // 获取 merchantId
        MerchantInfo merchantInfo = MerchantContext.get();
        MerchantShop merchantShop = merchantShopService.getByMerchantId(merchantInfo.getUserId());
        if (null != merchantShop) {
            return MerchantShopVO.convertFrom(merchantShop, MerchantShopVO.class);
        }
        return null;
    }

    /**
     * 按id查询店铺信息，提供给api项目
     */
    public MerchantShopVO getShopInfo(IdParam param) {
        MerchantShop merchantShop = merchantShopService.get(param.getId());
        if (null != merchantShop) {
            return MerchantShopVO.convertFrom(merchantShop, MerchantShopVO.class);
        }
        return null;
    }

    /**
     * 审核通过（更新店铺信息）
     */
    @Transactional
    public void shopAuditPass(IdParam param) {
        updateAuditStatusFrom(param.getId(), MerchantShopAuditStatusEnum.PASS.name(), MerchantShopAuditStatusEnum.WAIT.name());

        // 更新店铺信息
        // 查询店铺审核记录
        MerchantShopAudit merchantShopAudit = merchantShopAuditService.get(param.getId());
        // 查询商户店铺
        MerchantShop merchantShop = merchantShopService.getByMerchantId(merchantShopAudit.getMerchantId());
        // 将审核记录的信息，覆盖成商户店铺信息
        MerchantShop updateShop = BaseVO.convertFrom(merchantShopAudit, MerchantShop.class);
        // 设置店铺id，为了按主键更新
        updateShop.setId(merchantShop.getId());
        // 修改店铺状态为正常
        updateShop.setStatus(MerchantShopStatusEnum.NORMAL.name());
        merchantShopService.updateByPrimaryKeySelective(updateShop);
    }

    /**
     * 审核拒绝
     */
    public void shopAuditReject(IdParam param) {
        updateAuditStatusFrom(param.getId(), MerchantShopAuditStatusEnum.REJECT.name(), MerchantShopAuditStatusEnum.WAIT.name());
    }

    /**
     * 取消审核
     */
    public void shopAuditCancel(IdParam param) {
        updateAuditStatusFrom(param.getId(), MerchantShopAuditStatusEnum.CANCEL.name(), MerchantShopAuditStatusEnum.WAIT.name());
    }

    /**
     * 更新审核记录状态
     *
     * @param id              记录id
     * @param auditStatus     审核状态
     * @param fromAuditStatus 条件状态
     */
    private void updateAuditStatusFrom(Long id, String auditStatus, String fromAuditStatus) {
        // 获取管理员用户id
        Long auditBy = 1L;
        int rows = merchantShopAuditService.updateAuditStatusFrom(id, auditStatus, fromAuditStatus, auditBy);
        if (rows == 0) {
            throw new BusinessException(MerchantErrorCodeEnum.SHOP_AUDIT_STATUS_ERROR.getCode(), MerchantErrorCodeEnum.SHOP_AUDIT_STATUS_ERROR.getMsg());
        }
    }

}
