package com.jlu.common.utils;

import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.offbytwo.jenkins.model.BuildResult;

/**
 * Created by langshiquan on 18/1/20.
 */
public class BuildStatusUtils {
    public static PipelineJobStatus toJobStatus(BuildResult buildResult) {
        if (buildResult.equals(BuildResult.SUCCESS)) {
            return PipelineJobStatus.SUCCESS;
        }
        return PipelineJobStatus.FAILED;
    }
}
