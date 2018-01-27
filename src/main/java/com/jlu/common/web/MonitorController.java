package com.jlu.common.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by langshiquan on 18/1/28.
 */
@RestController
public class MonitorController {

    // TODO return String
    @RequestMapping("/monitor")
    public ResponseBean monitor() {
        return ResponseBean.TRUE;
    }
}
