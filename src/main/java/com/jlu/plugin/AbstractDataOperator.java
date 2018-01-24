package com.jlu.plugin;

import org.codehaus.jettison.json.JSONObject;

import java.lang.reflect.ParameterizedType;

/**
 * Job data operator
 *
 * @param <ConfT>  Job conf type
 * @param <BuildT> Job build type
 */
public abstract class AbstractDataOperator<ConfT, BuildT> {
    public Class getConfClass() {
        // 通过范型反射，获取在子类中定义的entityClass.
        return (Class<ConfT>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class getBuildClass() {
        // 通过范型反射，获取在子类中定义的entityClass.
        return (Class<BuildT>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public abstract Long saveConf(JSONObject json);

    public abstract ConfT getConf(Long id);

    public abstract BuildT getBuild(Long id);

    // 运行时更新pluginBuild
    public void updateBuild(BuildT buildT) {
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
