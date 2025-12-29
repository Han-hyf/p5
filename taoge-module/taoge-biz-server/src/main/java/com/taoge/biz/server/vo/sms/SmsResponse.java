package com.taoge.biz.server.vo.sms;

import lombok.Data;

/**
 * 短信发送结果统一处理类
 */
@Data
public class SmsResponse {
    // 短信模板id
    private String templateId;
    // 短信模板参数
    private Object templateParam;
    // 是否成功
    private boolean success;
    // 渠道
    private String channel;
    // 发送结果
    private String sendMessage;
}
