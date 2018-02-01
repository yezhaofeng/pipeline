package com.jlu.pipeline.job.bean;

import com.offbytwo.jenkins.model.BuildResult;

/**
 * Created by langshiquan on 18/1/10.
 */
public enum PipelineJobStatus {
    INIT, RUNNING, FAILED, SUCCESS;

    public static PipelineJobStatus fromJenkinsBuildStatus(BuildResult buildResult) {
        if (buildResult.equals(BuildResult.SUCCESS)) {
            return PipelineJobStatus.SUCCESS;
        }
        return PipelineJobStatus.FAILED;
    }
}
