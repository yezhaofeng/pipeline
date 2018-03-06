package com.jlu.plugin.runtime.service;

import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by Administrator on 2018/1/24.
 */
// 使用泛型接口，或者协变返回类型
public interface PluginValueGenerator<T> {
    T generator(JobBuild jobBuild);
}
