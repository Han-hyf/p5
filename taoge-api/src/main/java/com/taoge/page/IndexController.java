package com.taoge.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/")
public class IndexController {

    /**
     * 登录
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("forwardPage", "login");
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
        // ControllerUtil.cleanSysToken(response, commonConfig.getDomain());
        model.addAttribute("forwardPage", "login");
        return "login";
    }

    @RequestMapping("/register")
    public String register(Model model, HttpServletRequest request, HttpServletResponse response) {
        return "register";
    }


}
