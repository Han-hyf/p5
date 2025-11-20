package com.taoge.biz.server.vo.sms;

import lombok.Data;

@Data
public class SmsResponse {

    //短信模板id
    private String templateId;

    //短信模板参数
    private String templateParam;

    //是否成功
    private boolean success;

    //发送短信渠道
    private String channel;

}
