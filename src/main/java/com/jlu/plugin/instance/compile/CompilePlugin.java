package com.jlu.plugin.instance.compile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.plugin.AbstractDataOperator;
import com.jlu.plugin.AbstractExecutor;
import com.jlu.plugin.AbstractPlugin;
import com.jlu.plugin.bean.PluginConfig;
import com.jlu.plugin.bean.PluginType;
import com.jlu.plugin.instance.compile.model.CompileBuild;
import com.jlu.plugin.instance.compile.model.CompileConf;

/**
 * Created by yezhaofeng on 2019/1/20.
 */
@Service
public class CompilePlugin extends AbstractPlugin<CompileConf, CompileBuild> {

    private final PluginType pluginType = PluginType.COMPILE;
    private final PluginConfig pluginConfig = new PluginConfig(pluginType, pluginType.getPluginName());

    @Autowired
    private CompileDataOperator compileDataOperator;

    @Autowired
    private CompileExecutor compileExecutor;

    @Override
    public PluginConfig getConfig() {
        return pluginConfig;
    }

    @Override
    public AbstractExecutor getExecutor() {
        return compileExecutor;
    }

    @Override
    public AbstractDataOperator<CompileConf, CompileBuild> getDataOperator() {
        return compileDataOperator;
    }
}
