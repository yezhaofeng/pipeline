package com.jlu.plugin.runtime.service;

import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by Administrator on 2018/1/24.
 */
public interface PluginDefaultValueGenerator {
    Object generator(JobBuild jobBuild);
}
