package com.taoge.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@Controller
@RequestMapping(value = "/page/")
public class PageController {

    @RequestMapping("/{page}")
    public String simplePage(@PathVariable String page, HttpServletRequest request, HttpServletResponse response) {
        return page;
    }

    @RequestMapping("/{module}/{controller}")
    public String modulePage(@PathVariable String module, @PathVariable String controller, HttpServletRequest request, HttpServletResponse response) {
        return module + "/" + controller;
    }

    @RequestMapping("/{module}/{actionType}/{page}")
    public String simplePage(@PathVariable String module, @PathVariable String actionType, @PathVariable String page, HttpServletRequest request, HttpServletResponse response) {
        return module + "/" + actionType + "/" + page;
    }

    @RequestMapping("/{module}/{actionType}/{page}/{subPage}")
    public String simplePage(@PathVariable String module, @PathVariable String actionType, @PathVariable String page, @PathVariable String subPage, HttpServletRequest request, HttpServletResponse response) {
        return module + "/" + actionType + "/" + page + "/" + subPage;
    }
}
