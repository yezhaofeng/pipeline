package com.jlu.common.web;

import com.jlu.common.permission.annotations.PermissionAdmin;
import com.jlu.plugin.thread.PluginThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.permission.annotations.PermissionPass;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * Created by yezhaofeng on 2019/1/28.
 */
@ApiIgnore
@RestController
public class MonitorController {

    @Autowired
    private PluginThreadService pluginThreadService;

    @PermissionPass
    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public String monitor() {
        return "ok";
    }

    @PermissionAdmin
    @RequestMapping(value = "/threads", method = RequestMethod.GET)
    public Map<Long, Thread> threads() {
        return pluginThreadService.getJobBuildThreadMap();
    }

    @PermissionAdmin
    @RequestMapping(value = "/thread/{jobBuildId}", method = RequestMethod.GET)
    public Thread thread(@PathVariable Long jobBuildId) {
        return pluginThreadService.getJobBuildThread(jobBuildId);
    }

}
