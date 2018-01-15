package com.jlu.common.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/8/4.
 */
@Controller
public class HomeController {

    private Logger logger = LoggerFactory.getLogger(HomeController.class);
    @RequestMapping("")
    public String home() {
        logger.info("come a user");
        return "home";
    }
}
