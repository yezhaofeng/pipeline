package com.jlu.plugin;

import org.codehaus.jettison.json.JSONObject;

/**
 * Job data operator
 *
 * @param <ConfT>  Job conf type
 * @param <BuildT> Job build type
 */
public abstract class AbstractDataOperator<ConfT, BuildT> {

    public abstract Long saveConf(JSONObject json);

    public abstract ConfT getConf(Long id);

    public abstract BuildT getBuild(Long id);

    public void updateBuild(JSONObject json) {
    }

    public Long initPluginBuildByPluginConf(Long pluginConfId) {
        return -1L;
    }

    public BuildT getBriefBuild(Long id) {
        return getBuild(id);
    }

    public BuildT getDetailBuild(Long id) {
        return getBuild(id);
    }

}
