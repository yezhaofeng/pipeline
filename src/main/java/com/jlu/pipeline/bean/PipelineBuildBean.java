package com.jlu.pipeline.bean;

import java.util.List;

import com.jlu.github.model.GitHubCommit;
import com.jlu.pipeline.job.bean.JobBuildBean;
import com.jlu.pipeline.model.PipelineBuild;

/**
 * Created by yezhaofeng on 2019/1/19.
 */
public class PipelineBuildBean extends PipelineBuild {
    GitHubCommit gitHubCommit;
    List<JobBuildBean> jobBuildBeanList;

    public GitHubCommit getGitHubCommit() {
        return gitHubCommit;
    }

    public void setGitHubCommit(GitHubCommit gitHubCommit) {
        this.gitHubCommit = gitHubCommit;
    }

    public List<JobBuildBean> getJobBuildBeanList() {
        return jobBuildBeanList;
    }

    public void setJobBuildBeanList(List<JobBuildBean> jobBuildBeanList) {
        this.jobBuildBeanList = jobBuildBeanList;
    }
}
