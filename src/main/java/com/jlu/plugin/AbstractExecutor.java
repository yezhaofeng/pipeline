package com.jlu.plugin;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jlu.common.utils.JsonUtils;
import com.jlu.plugin.runtime.RuntimeRequire;
import com.jlu.plugin.runtime.bean.RunTimeBean;
import com.jlu.plugin.runtime.service.PluginDefaultValueGenerator;
import com.jlu.plugin.service.IPluginInfoService;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.plugin.bean.JobBuildContext;

/**
 * Job executor
 */
public abstract class AbstractExecutor {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private IPluginInfoService pluginInfoService;
    @Autowired
    protected IJobBuildService jobBuildService;

    public void executeJob(JobBuildContext context, JobBuild jobBuild) {
        jobBuild.setStartTime(new Date());
        jobBuild.setMessage(StringUtils.EMPTY);
        jobBuildService.saveOrUpdate(jobBuild);
        Long pluginBuildId = jobBuild.getPluginBuildId();
        if (pluginBuildId == null || pluginBuildId == -1L || pluginBuildId == 0L) {
            return;
        }
        // 运行时的参数保存到PluginBuild中
        Map<String, Object> runtimePluginParam = context.getRuntimePluginParam();
        AbstractDataOperator abstractDataOperator = pluginInfoService.getRealJobPlugin(jobBuild.getPluginType()).getDataOperator();
        Object pluginBean = abstractDataOperator.getBuild(jobBuild.getPluginBuildId());
        Class buildClass = abstractDataOperator.getBuildClass();

        Field[] fields = buildClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field filed = fields[i];
            RuntimeRequire runtimeRequire = filed.getAnnotation(RuntimeRequire.class);
            if (runtimeRequire != null) {
                try {
                    filed.setAccessible(true);
                    filed.set(pluginBean, runtimePluginParam.get(filed.getName()));
                } catch (Exception e) {
                    // TODO log
                    continue;
                }
            }
        }
        pluginInfoService.getRealJobPlugin(jobBuild.getPluginType()).getDataOperator().updateBuild(pluginBean);
        execute(context, jobBuild);
    }

    protected abstract void execute(JobBuildContext context, JobBuild jobBuild);

    public void handleCallback(JobBuild jobBuild) {
        jobBuildService.notifiedJobBuildUpdated(jobBuild, new HashMap());
    }
}
