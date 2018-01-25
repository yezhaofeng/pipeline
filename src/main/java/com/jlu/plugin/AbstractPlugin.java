package com.jlu.plugin;

import java.lang.reflect.ParameterizedType;

import javax.validation.constraints.NotNull;

import org.codehaus.jettison.json.JSONObject;

import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.bean.JobBuildContext;
import com.jlu.plugin.bean.PluginConfig;

/**
 * Agile Real Job Plugin
 *
 * @param <ConfT>   Job type
 * @param <BuildT> JobBuild type
 */
public abstract class AbstractPlugin<ConfT, BuildT> {
    public Class getConfClass() {
        // 通过范型反射，获取在子类中定义的entityClass.
        return (Class<ConfT>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class getBuildClass() {
        // 通过范型反射，获取在子类中定义的entityClass.
        return (Class<BuildT>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }
    /**
     * Get job common base config
     *
     * @return job config
     */
    public abstract PluginConfig getConfig();

    private final AbstractExecutor defaultExcutor = new AbstractExecutor() {

        @Override
        public void execute(JobBuildContext context, JobBuild jobBuild) {

        }
    };

    /**
     * Get job data operator, the default excutor implement do nothing
     *
     * @return excutor, not null
     */
    @NotNull
    public AbstractExecutor getExecutor() {
        return defaultExcutor;
    }

    private final AbstractDataOperator<ConfT, BuildT> defaultDataOperator = new AbstractDataOperator<ConfT, BuildT>() {
        @Override
        public Long saveConf(JSONObject json) {
            // do nothing
            return 0L;
        }

        @Override
        public ConfT getConf(Long id) {
            return null;
        }

        @Override
        public BuildT getBuild(Long id) {
            return null;
        }
    };

    /**
     * Get job data operator, the default operator implement do nothing
     *
     * @return data operator, not null
     */
    @NotNull
    public AbstractDataOperator<ConfT, BuildT> getDataOperator() {
        return defaultDataOperator;
    }

}
