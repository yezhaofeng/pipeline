package com.jlu.common.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by langshiquan on 18/1/28.
 */
@RestController
public class MonitorController {

    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public String monitor() {
        return "ok";
    }
}
