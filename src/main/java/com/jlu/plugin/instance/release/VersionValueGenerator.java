package com.jlu.plugin.instance.release;

import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.model.PipelineBuild;
import com.jlu.pipeline.service.IPipelineBuildService;
import com.jlu.plugin.runtime.service.PluginValueGenerator;
import com.jlu.plugin.instance.release.service.IReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/1/24.
 */
@Component
public class VersionValueGenerator implements PluginValueGenerator {
    @Autowired
    private IReleaseService releaseService;
    @Autowired
    private IPipelineBuildService pipelineBuildService;

    @Override
    public String generator(JobBuild jobBuild) {
        Long pipelineBuildId = jobBuild.getPipelineBuildId();
        PipelineBuild pipelineBuild = pipelineBuildService.get(pipelineBuildId);
        String maxVersion = releaseService.getMaxVersion(pipelineBuild.getModule());
        String version = releaseService.increaseVersion(maxVersion);
        return version;
    }
}
