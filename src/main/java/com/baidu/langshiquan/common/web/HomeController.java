package com.baidu.langshiquan.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/8/4.
 */
@Controller
public class HomeController {

    @RequestMapping("")
    public String home() {
        return "home";
    }
}
