package com.taoge.framework.controller;

import lombok.Builder;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author xuejingtao
 * since 2020/07/21 11:46
 **/
@Data
@Builder
public class BaseRequest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
}
