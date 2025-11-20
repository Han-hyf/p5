package com.taoge.biz.server.param.user;

import com.taoge.biz.common.enums.GenderEnum;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateUserInfoParam extends BaseParam {
    private String nickname;
    private GenderEnum gender;
    private Date birthday;
}
