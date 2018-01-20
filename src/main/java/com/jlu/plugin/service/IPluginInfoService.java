package com.jlu.plugin.service;

import java.util.List;

import com.jlu.plugin.AbstractPlugin;
import com.jlu.plugin.bean.PluginConfig;
import com.jlu.plugin.bean.PluginType;

/**
 * Created by langshiquan on 18/1/13.
 */
public interface IPluginInfoService {
    List<AbstractPlugin> getAllPlugin();

    List<PluginConfig> getAllPluginConf();

    void initJobPluginConfigs();

    AbstractPlugin getRealJobPlugin(PluginType pluginType);

}
