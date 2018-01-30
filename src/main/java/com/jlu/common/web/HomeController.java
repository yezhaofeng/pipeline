package com.jlu.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Administrator on 2017/8/4.
 */
@Controller
public class HomeController {

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login() {
        return "login";
    }

}
