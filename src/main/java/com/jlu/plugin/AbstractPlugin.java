package com.jlu.plugin;

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
