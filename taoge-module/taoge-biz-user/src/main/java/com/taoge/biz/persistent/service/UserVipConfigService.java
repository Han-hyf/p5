/*
 * @ClassName UserVipConfigService
 * @Description
 * @version 1.0
 * @Date 2023-11-06 21:28:21
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.UserVipConfigMapper;
import com.taoge.biz.persistent.entity.UserVipConfig;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserVipConfigService extends BaseService<UserVipConfig, UserVipConfigMapper> {

    /**
     * 添加会员配置
     */
    public void add(String vipName, BigDecimal vipPrice, String vipIcon, Integer vipDays, String vipDaysName, Integer vipLevel) {
        UserVipConfig add = new UserVipConfig();
        add.setVipName(vipName);
        add.setVipPrice(vipPrice);
        add.setVipIcon(vipIcon);
        add.setVipDays(vipDays);
        add.setVipDaysName(vipDaysName);
        add.setVipLevel(vipLevel);
        add.setSort(System.currentTimeMillis());
        insertSelective(add);
    }

    /**
     * 添加会员配置
     */
    public void update(Long id, String vipName, BigDecimal vipPrice, String vipIcon, Integer vipDays, String vipDaysName, Integer vipLevel) {
        UserVipConfig update = new UserVipConfig();
        update.setId(id);
        update.setVipName(vipName);
        update.setVipPrice(vipPrice);
        update.setVipIcon(vipIcon);
        update.setVipDays(vipDays);
        update.setVipDaysName(vipDaysName);
        update.setVipLevel(vipLevel);
        update.setSort(System.currentTimeMillis());
        updateByPrimaryKeySelective(update);
    }

    public void updateStatus(Long id, Boolean status) {
        UserVipConfig update = new UserVipConfig();
        update.setId(id);
        update.setStatus(status);
        updateByPrimaryKeySelective(update);
    }

    public void sort(List<Long> ids) {
        getMapper().sort(ids);
    }
}