package com.taoge.framework.controller;

import lombok.Data;

@Data
public class PageParam extends BaseParam {
    private int pageNum;
    private int pageSize;
}
