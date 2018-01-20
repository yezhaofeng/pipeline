package com.jlu.plugin.instance.compile;

import org.springframework.stereotype.Service;

import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.AbstractExecutor;
import com.jlu.plugin.bean.JobBuildContext;

/**
 * Created by langshiquan on 18/1/20.
 */
@Service
public class CompileAbstractExecutor extends AbstractExecutor {
    @Override
    public void execute(JobBuildContext context, JobBuild jobBuild) {

    }
}
