package com.jlu.plugin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jlu.common.permission.annotations.PermissionAdmin;
import com.jlu.common.web.ResponseBean;
import com.jlu.plugin.bean.PluginConfig;
import com.jlu.plugin.service.IPluginInfoService;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Job plugin controller
 */
@ApiIgnore
@Controller
@RequestMapping("/plugin/job")
public class JobPluginController {

    @Autowired
    private IPluginInfoService pluginInfoService;

    @RequestMapping(value = "/configs", method = RequestMethod.GET)
    @ResponseBody
    public List<PluginConfig> getConfigs() {
        return pluginInfoService.getAllPluginConf();
    }

    @PermissionAdmin
    @RequestMapping(value = "/configs/reload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBean reload() {
        pluginInfoService.initJobPluginConfigs();
        return ResponseBean.TRUE;
    }


}

