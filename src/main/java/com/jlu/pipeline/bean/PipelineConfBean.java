package com.jlu.pipeline.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.model.PipelineConf;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by yezhaofeng on 2019/1/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PipelineConfBean extends PipelineConf {

    @NotEmpty(message = "至少配置一个Job")
    private List<JobConfBean> jobConfs;

    public List<JobConfBean> getJobConfs() {
        return jobConfs;
    }

    public void setJobConfs(List<JobConfBean> jobConfs) {
        this.jobConfs = jobConfs;
    }
}
