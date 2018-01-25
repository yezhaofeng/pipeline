package com.jlu.plugin.runtime.service;

import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by Administrator on 2018/1/24.
 */
public interface PluginValueGenerator {
    Object generator(JobBuild jobBuild);
}
