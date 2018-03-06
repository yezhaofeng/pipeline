package com.jlu.plugin.runtime.service;

import com.jlu.pipeline.job.model.JobBuild;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/1/24.
 */
@Component
public class DefaultPluginValueGenerator implements PluginValueGenerator<String> {

    @Override
    public String generator(JobBuild jobBuild) {
        return StringUtils.EMPTY;
    }
}
