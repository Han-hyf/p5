package com.taoge.api.security.login;

import com.taoge.framework.controller.BaseVO;
import lombok.Data;

/**
 * 登录返回结果
 */
@Data
public class SecurityLoginVO extends BaseVO {
    private String token;
}
