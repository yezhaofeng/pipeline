package com.jlu.pipeline.job.bean;

import com.offbytwo.jenkins.model.BuildResult;

/**
 * Created by yezhaofeng on 2019/1/10.
 */
public enum PipelineJobStatus {
    INIT, RUNNING, FAILED, SUCCESS, CANCELED;

    public static PipelineJobStatus fromJenkinsBuildStatus(BuildResult buildResult) {
        if (buildResult.equals(BuildResult.SUCCESS)) {
            return PipelineJobStatus.SUCCESS;
        }
        return PipelineJobStatus.FAILED;
    }
}
