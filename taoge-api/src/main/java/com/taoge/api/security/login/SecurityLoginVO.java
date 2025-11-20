package com.taoge.api.security.login;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseVO;
import lombok.Data;

@Data
public class SecurityLoginVO extends BaseVO {
    @NotNull
    private String token;
}
