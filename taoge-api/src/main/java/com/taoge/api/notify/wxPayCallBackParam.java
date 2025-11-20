package com.taoge.api.notify;

import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class wxPayCallBackParam extends BaseParam {

    private String businessOrderSn;

}
