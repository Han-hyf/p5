package com.taoge.biz.server.vo.vip;

import com.taoge.framework.controller.BaseVO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserVipConfigVO extends BaseVO {private Long id;
    private String vipName;
    private BigDecimal vipPrice;
    private String vipIcon;
    private Integer vipDays;
    private String vipDaysName;
    private Integer vipLevel;
    private Boolean status;
    private Long sort;
}
