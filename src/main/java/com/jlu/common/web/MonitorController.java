package com.jlu.common.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.permission.annotations.PermissionPass;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by langshiquan on 18/1/28.
 */
@ApiIgnore
@PermissionPass
@RestController
public class MonitorController {

    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public String monitor() {
        return "ok";
    }
}
