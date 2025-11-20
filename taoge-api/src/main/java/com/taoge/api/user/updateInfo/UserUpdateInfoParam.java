package com.taoge.api.user.updateInfo;

import com.taoge.biz.common.enums.GenderEnum;
import com.taoge.framework.annotation.Range;
import com.taoge.framework.annotation.Size;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;


import java.util.Date;

@Data
public class UserUpdateInfoParam extends BaseParam {


    @Size(min = 2,max = 8,name = "昵称")
    private String nickname;
    @Range(clazz = GenderEnum.class,key = "gender", errorMsg = "性别选择错误")
    private Integer gender;
    @com.taoge.framework.annotation.Date(format = "yyyy-MM-dd",errorMsg = "生日日期格式不正确")
    private Date birthday;

}
