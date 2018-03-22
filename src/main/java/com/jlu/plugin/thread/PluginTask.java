package com.jlu.plugin.thread;

import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.common.service.ServiceBeanFactory;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.bean.JobBuildContext;
import com.jlu.plugin.bean.PluginType;

/**
 * Created by Administrator on 2018/3/19.
 */
public class PluginTask implements Runnable {
    private JobBuildContext jobBuildContext;
    private JobBuild jobBuild;
    private PluginType pluginType;

    public PluginTask(JobBuildContext jobBuildContext, JobBuild jobBuild) {
        this.jobBuildContext = jobBuildContext;
        this.jobBuild = jobBuild;
        pluginType = jobBuild.getPluginType();
    }

    // 此构造方法构造的对象不能用于run
    public PluginTask(JobBuild jobBuild) {
        this.jobBuild = jobBuild;
    }

    @Override
    public void run() {
        if (jobBuild == null || jobBuildContext == null) {
            throw new PipelineRuntimeException("非法请求");
        }
        ServiceBeanFactory.getPluginThreadService().register(jobBuild.getId(), Thread.currentThread());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            ServiceBeanFactory.getJobBuildService().notifiedJobBuildStartCanceled(jobBuild);
            return;
        }
        try {
            ServiceBeanFactory.getPluginInfoService().getRealJobPlugin(pluginType).getExecutor().executeJob(jobBuildContext, jobBuild);
            checkInterrupted();
        } finally {
            ServiceBeanFactory.getPluginThreadService().destroy(jobBuild.getId());
        }
    }

    private void checkInterrupted() {
        if (Thread.currentThread().isInterrupted()) {
            ServiceBeanFactory.getPluginInfoService().getRealJobPlugin(pluginType).getExecutor().cancel(jobBuild);
            ServiceBeanFactory.getJobBuildService().notifiedJobBuildCanceled(jobBuild);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginTask that = (PluginTask) o;

        return jobBuild.equals(that.jobBuild);

    }

    @Override
    public int hashCode() {
        return jobBuild.hashCode();
    }
}
