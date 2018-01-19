package com.jlu.plugin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jlu.common.web.ResponseBean;
import com.jlu.plugin.bean.PluginConfig;
import com.jlu.plugin.service.IPluginInfoService;

/**
 * Job plugin controller
 */
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

    @RequestMapping(value = "/configs/reload", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBean reload() {
        pluginInfoService.initJobPluginConfigs();
        return ResponseBean.TRUE;
    }

}

