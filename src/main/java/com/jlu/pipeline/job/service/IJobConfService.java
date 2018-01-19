package com.jlu.pipeline.job.service;

import java.util.List;

import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.model.JobConf;

/**
 * Created by langshiquan on 18/1/14.
 */
public interface IJobConfService {

    JobConf processJobWithTransaction(JobConfBean jobConfBean, Long pipelineConfId);

    void processJobWithTransaction(List<JobConfBean> jobConfBeans, Long pipelineConfId);

    List<JobConfBean> getJobConfs(Long pipelineConfId);
}
