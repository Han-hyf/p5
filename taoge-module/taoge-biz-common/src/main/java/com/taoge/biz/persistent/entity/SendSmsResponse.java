package com.taoge.biz.persistent.entity;

import lombok.Data;

@Data
public class SendSmsResponse {
    private String mobile;
    private String templateId;
    private String code;
}
