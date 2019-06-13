package com.jlu.plugin.instance.release;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.plugin.AbstractDataOperator;
import com.jlu.plugin.AbstractExecutor;
import com.jlu.plugin.AbstractPlugin;
import com.jlu.plugin.bean.PluginConfig;
import com.jlu.plugin.bean.PluginType;
import com.jlu.plugin.instance.compile.model.CompileBuild;
import com.jlu.plugin.instance.compile.model.CompileConf;
import com.jlu.plugin.instance.release.model.ReleaseBuild;
import com.jlu.plugin.instance.release.model.ReleaseConf;

/**
 * Created by yezhaofeng on 2019/1/20.
 */
@Service
public class ReleasePlugin extends AbstractPlugin<ReleaseConf, ReleaseBuild> {

    private final PluginType pluginType = PluginType.RELEASE;
    private final PluginConfig pluginConfig = new PluginConfig(pluginType, pluginType.getPluginName());

    @Autowired
    private ReleaseDataOperator releaseDataOperator;

    @Autowired
    private ReleaseExecutor releaseExecutor;

    @Override
    public PluginConfig getConfig() {
        return pluginConfig;
    }

    @Override
    public AbstractExecutor getExecutor() {
        return releaseExecutor;
    }

    @Override
    public AbstractDataOperator<ReleaseConf, ReleaseBuild> getDataOperator() {
        return releaseDataOperator;
    }
}
