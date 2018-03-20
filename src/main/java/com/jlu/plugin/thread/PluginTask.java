package com.jlu.plugin.thread;

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

    @Override
    public void run() {
        ServiceBeanFactory.getPluginThreadService().register(jobBuild.getId(), Thread.currentThread());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ServiceBeanFactory.getPluginInfoService().getRealJobPlugin(pluginType).getExecutor().executeJob(jobBuildContext, jobBuild);
        ServiceBeanFactory.getPluginThreadService().destroy(jobBuild.getId());

    }
}
