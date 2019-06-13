package com.jlu.pipeline.job.service;

import java.util.List;
import java.util.Map;

import com.jlu.pipeline.job.bean.JobBuildBean;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.bean.TriggerMode;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.bean.PluginType;
import com.jlu.plugin.runtime.bean.RunTimeBean;

/**
 * Created by Administrator on 2019/1/18.
 */
public interface IJobBuildService {
    Long initBuild(JobConfBean jobConfBean, Long pipelineBuildId, Long upStreamJobBuildId, Map<String, String> params);

    // 当构建整条流水线的时候执行
    void buildTopJob(Long pipelineBuildId, TriggerMode triggerMode, String triggerUser);

    void build(Long jobBuildId, Map<String, String> execParam, Map<String, Object> runtimeJobParam, TriggerMode triggerMode, String triggerUser);

    /**
     * @return 是否真正取消成功
     */

    boolean cancel(Long jobBuildId);

    void notifiedJobBuildStarFailed(JobBuild jobBuild);

    void notifiedJobBuildStartCanceled(JobBuild jobBuild);

    void notifiedJobBuildCanceled(JobBuild jobBuild);

    void notifiedJobBuildFinished(JobBuild jobBuild, Map<String, String> newOutParams);

    List<JobBuildBean> getJobBuildBeans(Long pipelineBuildId);

    JobBuildBean getWithPluginBuild(Long jobBuildId);

    void saveOrUpdate(JobBuild jobBuild);

    List<RunTimeBean> getRuntimeRequire(Long jobBuildId);

    JobBuild getLastSuccBuild(String module, String commitId, PluginType pluginType, Long currentPipelineBuildId);
}
