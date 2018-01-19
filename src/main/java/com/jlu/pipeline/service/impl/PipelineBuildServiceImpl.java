package com.jlu.pipeline.service.impl;

import com.jlu.common.cookies.LoginHelper;
import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.github.model.GitHubCommit;
import com.jlu.github.service.IGitHubCommitService;
import com.jlu.pipeline.dao.IPipelineBuildDao;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.bean.TriggerMode;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.pipeline.job.service.IJobConfService;
import com.jlu.pipeline.model.PipelineBuild;
import com.jlu.pipeline.model.PipelineConf;
import com.jlu.pipeline.service.IPipelineConfService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.pipeline.service.IPipelineBuildService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class PipelineBuildServiceImpl implements IPipelineBuildService {
    @Autowired
    private IPipelineConfService pipelineConfService;

    @Autowired
    private IJobConfService jobConfService;

    @Autowired
    private IJobBuildService jobBuildService;

    @Autowired
    private IGitHubCommitService gitHubCommitService;

    @Autowired
    private IPipelineBuildDao IPipelineBuildDao;

    /**
     * 根据confId找到最新一次commit进行构建
     *
     * @param pipelineConfId
     */
    @Override
    public void build(Long pipelineConfId) {
        PipelineConf pipelineConf = pipelineConfService.getPipelineConf(pipelineConfId);
        Long pipelineBuildId = initPipeline(pipelineConf);
        jobBuildService.buildTopJob(pipelineBuildId);
    }

    /**
     * 根据confId和triggerId执行流水线
     *
     * @param pipelineConfId
     * @param triggerId
     */
    @Override
    public void build(Long pipelineConfId, Long triggerId) {
        GitHubCommit gitHubCommit = gitHubCommitService.get(triggerId);
        if (gitHubCommit == null) {
            throw new PipelineRuntimeException("TODO");
        }
        build(pipelineConfId, gitHubCommit);
    }

    /**
     * 根据confId和具体的commit执行流水线
     *
     * @param pipelineConfId
     * @param gitHubCommit
     */
    @Override
    public void build(Long pipelineConfId, GitHubCommit gitHubCommit) {
        PipelineConf pipelineConf = pipelineConfService.getPipelineConf(pipelineConfId);
        Long pipelineBuildId = initPipeline(pipelineConf, gitHubCommit);
        jobBuildService.buildTopJob(pipelineBuildId);
    }


    /**
     * 用最新的commit初始化流水线
     *
     * @param pipelineConf
     * @return pipelineBuild
     */
    @Override
    public Long initPipeline(PipelineConf pipelineConf) {
        Long pipelineConfId = pipelineConf.getId();
        String module = pipelineConf.getModule();
        String userName = pipelineConf.getCreateUser();
        GitHubCommit gitHubCommit = gitHubCommitService.getLastestCommit(module, userName);
        PipelineBuild pipelineBuild = initPipelineBuildByCommit(gitHubCommit, pipelineConfId, TriggerMode.MANUAL, LoginHelper.getLoginerUserName());
        Map<String, Object> params = initJobParams(pipelineBuild);
        IPipelineBuildDao.save(pipelineBuild);
        Long pipelineBuildId = pipelineBuild.getId();
        List<JobConfBean> jobConfBeanList = jobConfService.getJobConfs(pipelineConfId);
        initJobBuilds(pipelineBuildId, jobConfBeanList, params);
        return pipelineBuildId;
    }

    /**
     * 根据具体的commit初始化流水线
     *
     * @param pipelineConf
     * @param gitHubCommit
     * @return pipelineBuild
     */
    @Override
    public Long initPipeline(PipelineConf pipelineConf, GitHubCommit gitHubCommit) {
        PipelineBuild pipelineBuild = initPipelineBuildByCommit(gitHubCommit, pipelineConf.getId(), TriggerMode.AUTO, StringUtils.EMPTY);
        Map<String, Object> params = initJobParams(pipelineBuild);
        List<JobConfBean> jobConfBeanList = jobConfService.getJobConfs(pipelineConf.getId());
        IPipelineBuildDao.save(pipelineBuild);
        Long pipelineBuildId = pipelineBuild.getId();
        initJobBuilds(pipelineBuildId, jobConfBeanList, params);
        return pipelineBuildId;
    }

    private void initJobBuilds(Long pipelineBuildId, List<JobConfBean> jobConfBeanList, Map<String, Object> params) {
        Long upStreamJobBuildId = 0L;
        for (JobConfBean jobConfBean : jobConfBeanList) {
            Long jobBuildId = jobBuildService.init(jobConfBean, pipelineBuildId, upStreamJobBuildId, params);
            upStreamJobBuildId = jobBuildId;
        }
    }

    /**
     * 初始化pipelineBuild对象
     *
     * @param gitHubCommit
     * @param pipelineConfId
     * @param triggerMode
     * @param triggerUser
     * @return
     */
    private PipelineBuild initPipelineBuildByCommit(GitHubCommit gitHubCommit, Long pipelineConfId, TriggerMode triggerMode, String triggerUser) {
        PipelineBuild pipelineBuild = new PipelineBuild();
        pipelineBuild.setOwner(gitHubCommit.getOwner());
        pipelineBuild.setBranch(gitHubCommit.getBranch());
        Long buildNumber = IPipelineBuildDao.getNextBuildNumber(gitHubCommit.getOwner(), gitHubCommit.getModule());
        pipelineBuild.setBuildNumber(buildNumber);
        pipelineBuild.setModule(gitHubCommit.getModule());
        pipelineBuild.setBranch(gitHubCommit.getBranch());
        pipelineBuild.setCheckinAuthor(gitHubCommit.getCommitter());
        pipelineBuild.setStartTime(new Date());
        pipelineBuild.setCommitId(gitHubCommit.getCommitId());
        pipelineBuild.setTriggerMode(triggerMode);
        if (StringUtils.isBlank(triggerUser)) {
            pipelineBuild.setTriggerUser(gitHubCommit.getCommitter());
        } else {
            pipelineBuild.setTriggerUser(triggerUser);
        }
        pipelineBuild.setPipelineJobStatus(PipelineJobStatus.INIT);
        pipelineBuild.setCheckinAuthor(gitHubCommit.getCommitter());
        pipelineBuild.setTriggerId(gitHubCommit.getId());
        pipelineBuild.setPipelineConfId(pipelineConfId);
        return pipelineBuild;
    }

    private Map<String, Object> initJobParams(PipelineBuild pipelineBuild) {

        return null;
    }

}
