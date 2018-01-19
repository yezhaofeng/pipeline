package com.jlu.plugin.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.plugin.AbstractPlugin;
import com.jlu.plugin.bean.PluginConfig;
import com.jlu.plugin.bean.PluginType;
import com.jlu.plugin.dao.IPluginInfoDao;
import com.jlu.plugin.model.PluginInfo;
import com.jlu.plugin.service.IPluginInfoService;

/**
 * Created by langshiquan on 18/1/13.
 */
@Service
public class PluginInfoServiceImpl implements IPluginInfoService {

    private List<PluginConfig> pluginConfigs;

    @Autowired
    private List<AbstractPlugin> jobPlugins;

    @Autowired
    private IPluginInfoDao pluginInfoDao;

    @Override
    public List<AbstractPlugin> getAllPlugin() {
        return jobPlugins;
    }

    @Override
    public List<PluginConfig> getAllPluginConf() {
        return pluginConfigs;
    }

    @Override
    @PostConstruct
    public void initJobPluginConfigs() {
        List<PluginConfig> pluginConfigList = new LinkedList<>();
        for (AbstractPlugin abstractPlugin : jobPlugins) {
            PluginConfig pluginConfig = abstractPlugin.getConfig();
            mergeWithDb(pluginConfig);
            pluginConfigList.add(pluginConfig);
        }
        this.pluginConfigs = pluginConfigList;
    }

    // 经常变动的描述信息存在DB里
    private void mergeWithDb(PluginConfig pluginConfig) {
        PluginType pluginType = pluginConfig.getPluginType();
        PluginInfo pluginInfo = pluginInfoDao.findByJobType(pluginType.toString());
        if (pluginInfo == null) {
            return;
        }
        pluginConfig.setIcon(pluginInfo.getIcon());
        pluginConfig.setLink(pluginInfo.getWiki());
        pluginConfig.setPriority(pluginInfo.getPriority());
        pluginConfig.setDescription(pluginConfig.getDescription());
    }

    @Override
    public AbstractPlugin getRealJobPlugin(PluginType pluginType) {
        for (AbstractPlugin jobPlugin : jobPlugins) {
            if (jobPlugin.getConfig().getPluginType().equals(pluginType)) {
                return jobPlugin;
            }
        }
        return null;
    }
}
