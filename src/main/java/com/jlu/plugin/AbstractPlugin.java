package com.jlu.plugin;

import javax.validation.constraints.NotNull;

import org.codehaus.jettison.json.JSONObject;

import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.bean.JobBuildContext;
import com.jlu.plugin.bean.PluginConfig;

/**
 * Agile Real Job Plugin
 *
 * @param <JobT>   Job type
 * @param <BuildT> JobBuild type
 */
public abstract class AbstractPlugin<JobT, BuildT> {

    /**
     * Get job common base config
     *
     * @return job config
     */
    public abstract PluginConfig getConfig();

    private final IExecutor defaultExcutor = new IExecutor() {

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
    public IExecutor getExecutor() {
        return defaultExcutor;
    }

    private final IDataOperator<JobT, BuildT> defaultDataOperator = new IDataOperator<JobT, BuildT>() {
        @Override
        public Long saveJob(JSONObject json) {
            // do nothing
            return 0L;
        }

        @Override
        public JobT getJob(Long id) {
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
    public IDataOperator<JobT, BuildT> getDataOperator() {
        return defaultDataOperator;
    }

}
