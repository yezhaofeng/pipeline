package com.jlu.pipeline.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.branch.bean.BranchType;
import com.jlu.common.aop.annotations.LogExecTime;
import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.common.interceptor.UserLoginHelper;
import com.jlu.common.utils.DateUtils;
import com.jlu.common.utils.PipelineConfigReader;
import com.jlu.common.utils.PipelineUtils;
import com.jlu.github.model.GitHubCommit;
import com.jlu.github.service.IGitHubCommitService;
import com.jlu.pipeline.bean.PipelineBuildBean;
import com.jlu.pipeline.dao.IPipelineBuildDao;
import com.jlu.pipeline.job.bean.JobBuildBean;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.bean.JobParameter;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.bean.TriggerMode;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.pipeline.job.service.IJobConfService;
import com.jlu.pipeline.model.PipelineBuild;
import com.jlu.pipeline.model.PipelineConf;
import com.jlu.pipeline.service.IPipelineBuildService;
import com.jlu.pipeline.service.IPipelineConfService;

/**
 * Created by yezhaofeng on 2019/1/14.
 */
@Service
public class PipelineBuildServiceImpl implements IPipelineBuildService {

    private final Logger logger = LoggerFactory.getLogger(PipelineBuildServiceImpl.class);
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

    @Autowired
    private IPipelineBuildDao pipelineBuildDao;

    /**
     * 根据confId找到最新一次commit进行构建
     *
     * @param pipelineConfId
     */
    @Override
    public void build(Long pipelineConfId) {
        PipelineConf pipelineConf = pipelineConfService.getPipelineConf(pipelineConfId);
        Long pipelineBuildId = initPipelineBuild(pipelineConf);
        jobBuildService.buildTopJob(pipelineBuildId, TriggerMode.AUTO, UserLoginHelper.getLoginUserName());
    }

    @Override
    public void rebuild(Long triggerId) {
        GitHubCommit gitHubCommit = gitHubCommitService.get(triggerId);
        String module = gitHubCommit.getModule();
        BranchType branchType = gitHubCommit.getBranchType();
        PipelineConf pipelineConf = pipelineConfService.getPipelineConf(module, branchType);
        Long pipelineBuildId = initPipelineBuild(pipelineConf);
        jobBuildService.buildTopJob(pipelineBuildId, TriggerMode.AUTO, UserLoginHelper.getLoginUserName());
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
            throw new PipelineRuntimeException("未找到提交记录");
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
        logger.info("build confId-{} pipeline, github commit :{}", gitHubCommit);
        PipelineConf pipelineConf = pipelineConfService.getPipelineConf(pipelineConfId);
        if (pipelineConf == null) {
            throw new PipelineRuntimeException("未找到流水线配置");
        }
        Long pipelineBuildId = initPipelineBuild(pipelineConf, gitHubCommit);
        jobBuildService.buildTopJob(pipelineBuildId, TriggerMode.AUTO, gitHubCommit.getCommitter());
    }


    /**
     * 用最新的commit初始化流水线
     *
     * @param pipelineConf
     * @return pipelineBuild
     */
    @LogExecTime
    @Override
    public Long initPipelineBuild(PipelineConf pipelineConf) {
        if (pipelineConf == null) {
            throw new PipelineRuntimeException("未找到配置");
        }
        Long pipelineConfId = pipelineConf.getId();
        String module = pipelineConf.getModule();
        GitHubCommit gitHubCommit = gitHubCommitService.getLastestCommit(module);
        if (gitHubCommit == null) {
            throw new PipelineRuntimeException("无提交信息");
        }
        PipelineBuild pipelineBuild = initPipelineBuildByCommit(gitHubCommit, pipelineConf, TriggerMode.MANUAL,
                UserLoginHelper.getLoginUserName());
        IPipelineBuildDao.saveOrUpdate(pipelineBuild);
        Map<String, String> params = initJobParams(pipelineBuild, gitHubCommit);

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
    @LogExecTime
    @Override
    public Long initPipelineBuild(PipelineConf pipelineConf, GitHubCommit gitHubCommit) {
        PipelineBuild pipelineBuild =
                initPipelineBuildByCommit(gitHubCommit, pipelineConf, TriggerMode.AUTO, StringUtils.EMPTY);
        IPipelineBuildDao.saveOrUpdate(pipelineBuild);
        Map<String, String> params = initJobParams(pipelineBuild, gitHubCommit);
        List<JobConfBean> jobConfBeanList = jobConfService.getJobConfs(pipelineConf.getId());

        Long pipelineBuildId = pipelineBuild.getId();
        initJobBuilds(pipelineBuildId, jobConfBeanList, params);
        return pipelineBuildId;
    }

    @Override
    public List<PipelineBuildBean> getPipelineBuildBean(String module, BranchType branchType, int offset, int limit) {
        PipelineConf pipelineConf = pipelineConfService.getPipelineConf(module, branchType);
        if (pipelineConf == null) {
            return new ArrayList<>(0);
        }
        return getPipelineBuildBean(pipelineConf.getId(), offset, limit);
    }

    @Override
    public List<PipelineBuildBean> getPipelineBuildBean(String module, BranchType branchType, String branchName, int offset, int limit) {
        PipelineConf pipelineConf = pipelineConfService.getPipelineConf(module, branchType);
        if (pipelineConf == null) {
            return new ArrayList<>(0);
        }
        return getPipelineBuildBean(pipelineConf.getId(), branchName, offset, limit);
    }


    @Override
    public List<PipelineBuildBean> getPipelineBuildBean(Long pipelineConfId) {
        List<PipelineBuild> pipelineBuilds = pipelineBuildDao.get(pipelineConfId);
        return assemblePipelineBuildBean(pipelineBuilds);
    }

    @Override
    public List<PipelineBuildBean> getPipelineBuildBean(Long pipelineConfId, int offset, int limit) {
        List<PipelineBuild> pipelineBuilds = pipelineBuildDao.get(pipelineConfId, offset, limit);
        return assemblePipelineBuildBean(pipelineBuilds);
    }

    @Override
    public List<PipelineBuildBean> getPipelineBuildBean(Long pipelineConfId, String branchName) {
        List<PipelineBuild> pipelineBuilds = pipelineBuildDao.get(pipelineConfId, branchName);
        return assemblePipelineBuildBean(pipelineBuilds);
    }

    @Override
    public List<PipelineBuildBean> getPipelineBuildBean(Long pipelineConfId, String branchName, int offset, int limit) {
        List<PipelineBuild> pipelineBuilds = pipelineBuildDao.get(pipelineConfId, branchName, offset, limit);
        return assemblePipelineBuildBean(pipelineBuilds);
    }

    private List<PipelineBuildBean> assemblePipelineBuildBean(List<PipelineBuild> pipelineBuilds) {
        List<PipelineBuildBean> pipelineBuildBeans = new LinkedList<>();
        for (PipelineBuild pipelineBuild : pipelineBuilds) {
            PipelineBuildBean pipelineBuildBean = new PipelineBuildBean();
            BeanUtils.copyProperties(pipelineBuild, pipelineBuildBean);
            Long triggerId = pipelineBuild.getTriggerId();
            GitHubCommit githubCommit = gitHubCommitService.get(triggerId);
            pipelineBuildBean.setGitHubCommit(githubCommit);
            List<JobBuildBean> jobBuildBeans = jobBuildService.getJobBuildBeans(pipelineBuild.getId());
            pipelineBuildBean.setJobBuildBeanList(jobBuildBeans);
            pipelineBuildBeans.add(pipelineBuildBean);
        }
        return pipelineBuildBeans;
    }

    @Override
    public PipelineBuild get(Long pipelineBuildId) {
        return pipelineBuildDao.findById(pipelineBuildId);
    }

    private void initJobBuilds(Long pipelineBuildId, List<JobConfBean> jobConfBeanList, Map<String, String> params) {
        Long upStreamJobBuildId = 0L;
        for (JobConfBean jobConfBean : jobConfBeanList) {
            Long jobBuildId = jobBuildService.initBuild(jobConfBean, pipelineBuildId, upStreamJobBuildId, params);
            upStreamJobBuildId = jobBuildId;
        }
    }

    /**
     * 初始化pipelineBuild对象
     *
     * @param gitHubCommit
     * @param pipelineConf
     * @param triggerMode
     * @param triggerUser
     * @return
     */
    private PipelineBuild initPipelineBuildByCommit(GitHubCommit gitHubCommit, PipelineConf pipelineConf,
                                                    TriggerMode triggerMode,
                                                    String triggerUser) {
        PipelineBuild pipelineBuild = new PipelineBuild();
        pipelineBuild.setBranch(gitHubCommit.getBranch());
        Long buildNumber = IPipelineBuildDao.getNextBuildNumber(gitHubCommit.getModule());
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
        pipelineBuild.setPipelineStatus(PipelineJobStatus.INIT);
        pipelineBuild.setCheckinAuthor(gitHubCommit.getCommitter());
        pipelineBuild.setTriggerId(gitHubCommit.getId());
        pipelineBuild.setPipelineConfId(pipelineConf.getId());
        pipelineBuild.setName(pipelineConf.getName());
        return pipelineBuild;
    }

    private Map<String, String> initJobParams(PipelineBuild pipelineBuild, GitHubCommit gitHubCommit) {
        Map<String, String> params = new HashedMap();
        params.put(JobParameter.PIPELINE_BRANCH, pipelineBuild.getBranch());
        params.put(JobParameter.PIPELINE_BUILD_ID, String.valueOf(pipelineBuild.getId()));
        params.put(JobParameter.PIPELINE_BUILD_NUMBER, String.valueOf(pipelineBuild.getBuildNumber()));
        params.put(JobParameter.PIPELINE_COMMIT_COMMENTS, gitHubCommit.getCommits());
        params.put(JobParameter.PIPELINE_MODULE_NAME, pipelineBuild.getModule());
        params.put(JobParameter.PIPELINE_REPOSITORY_GITHUB_URL,
                String.format(PipelineConfigReader.getConfigValueByKey("github.base.repo"), gitHubCommit.getOwner(),
                        PipelineUtils.getRepoNameByModule(gitHubCommit.getModule())));
        params.put(JobParameter.PIPELINE_START_TIME, DateUtils.format(pipelineBuild.getStartTime()));
        params.put(JobParameter.PIPELINE_TRIGGER_USER, pipelineBuild.getTriggerUser());
        params.put(JobParameter.PIPELINE_CHECKIN_AUTHOR, pipelineBuild.getCheckinAuthor());
        params.put(JobParameter.PIPELINE_COMMIT_ID, pipelineBuild.getCommitId());
        params.put(JobParameter.PIPELINE_TRIGGER_MODE, pipelineBuild.getTriggerMode().name());
        return params;
    }

}
