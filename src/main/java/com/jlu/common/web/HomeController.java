package com.jlu.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jlu.common.permission.annotations.PermissionPass;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Administrator on 2019/8/4.
 */

@ApiIgnore
@Controller
public class HomeController {

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    @PermissionPass
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/intro",method = RequestMethod.GET)
    public String intro() {
        return "intro";
    }


}
