package com.jlu.pipeline.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.branch.bean.BranchType;
import com.jlu.common.interceptor.UserLoginHelper;
import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.common.utils.CollUtils;
import com.jlu.pipeline.bean.PipelineConfBean;
import com.jlu.pipeline.dao.IPipelineConfDao;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.bean.TriggerMode;
import com.jlu.pipeline.job.service.IJobConfService;
import com.jlu.pipeline.model.PipelineConf;
import com.jlu.pipeline.service.IPipelineConfService;
import com.jlu.plugin.bean.PluginType;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class PipelineConfServiceImpl implements IPipelineConfService {

    @Autowired
    private IPipelineConfDao pipelineConfDao;

    @Autowired
    private IJobConfService jobConfService;

    @Override
    public void processPipelineWithTransaction(PipelineConfBean pipelineConfBean) {
        PipelineConf pipelineConf = pipelineConfDao.get(pipelineConfBean.getModule(), pipelineConfBean.getBranchType());
        if (pipelineConf == null) {
            throw new PipelineRuntimeException("未找到流水线配置");
        }
        BeanUtils.copyProperties(pipelineConfBean, pipelineConf);
        pipelineConf.setLastModifiedUser(UserLoginHelper.getLoginUserName());
        pipelineConf.setLastModifiedTime(new Date());
        pipelineConfDao.saveOrUpdate(pipelineConf);
        List<JobConfBean> jobConfBeans = pipelineConfBean.getJobConfs();
        jobConfService.processJobWithTransaction(jobConfBeans, pipelineConf.getId());
    }

    @Override
    public PipelineConfBean getPipelineConfBean(Long pipelineConfId) {
        PipelineConfBean pipelineConfBean = new PipelineConfBean();
        if (pipelineConfId == null || pipelineConfId == 0L) {
            return null;
        }
        PipelineConf pipelineConf = pipelineConfDao.findById(pipelineConfId);
        if (pipelineConf == null) {
            return null;
        }
        BeanUtils.copyProperties(pipelineConf, pipelineConfBean);
        List<JobConfBean> jobConfs = jobConfService.getJobConfs(pipelineConfId);
        pipelineConfBean.setJobConfs(jobConfs);
        return pipelineConfBean;
    }

    @Override
    public PipelineConf getPipelineConf(Long pipelineConfId) {
        return pipelineConfDao.findById(pipelineConfId);
    }

    @Override
    public PipelineConf getPipelineConf(String module, String branchName) {
        return pipelineConfDao.get(module, BranchType.parseType(branchName));
    }

    @Override
    public PipelineConf getPipelineConf(String module, BranchType branchType) {
        return pipelineConfDao.get(module, branchType);
    }

    private final String DEFAULT_MASTER_PIPELINE_NAME = "MasterPipeline";
    private final String DEFAULT_BRANCH_PIPELINE_NAME = "BranchPipeline";
    private final String DEFAULT_MASTER_PIPELINE_REMARK = "主干";
    private final String DEFAULT_BRANCH_PIPELINE_REMARK = "分支";
    private final String DEFAULT_COMPILE_JOB_NAME = "构建";
    private final String DEFAULT_RELEASE_JOB_NAME = "发版";

    // TODO 锁
    @Override
    public void initDefaultConf(String module) {
        List<PipelineConf> pipelineConfs = pipelineConfDao.get(module);
        if (!CollUtils.isEmpty(pipelineConfs)) {
            return;
        }
        // 主干流水线
        PipelineConfBean masterPipelineConf = initDefaultPipelineConf(module, DEFAULT_MASTER_PIPELINE_NAME,
                BranchType.TRUNK, DEFAULT_MASTER_PIPELINE_REMARK);
        processPipelineForInitWithTransaction(masterPipelineConf);
        // 分支流水线
        PipelineConfBean branchPipelineConf = initDefaultPipelineConf(module, DEFAULT_BRANCH_PIPELINE_NAME,
                BranchType.BRANCH, DEFAULT_BRANCH_PIPELINE_REMARK);
        processPipelineForInitWithTransaction(branchPipelineConf);
    }
    private void processPipelineForInitWithTransaction(PipelineConfBean pipelineConfBean) {
        PipelineConf pipelineConf = null;
        Long pipelineConfId = pipelineConfBean.getId();
        if (pipelineConfId != null) {
            pipelineConf = pipelineConfDao.findById(pipelineConfId);
        }

        if (pipelineConf != null) {
            BeanUtils.copyProperties(pipelineConfBean, pipelineConf);
            pipelineConf.setId(pipelineConfId);
        } else {
            pipelineConf = new PipelineConf();
            BeanUtils.copyProperties(pipelineConfBean, pipelineConf);
            pipelineConf.setCreateTime(new Date());
            pipelineConf.setCreateUser(UserLoginHelper.getLoginUserName());
        }

        pipelineConf.setLastModifiedUser(UserLoginHelper.getLoginUserName());
        pipelineConf.setLastModifiedTime(new Date());
        pipelineConfDao.saveOrUpdate(pipelineConf);
        List<JobConfBean> jobConfBeans = pipelineConfBean.getJobConfs();
        jobConfService.processJobWithTransaction(jobConfBeans, pipelineConf.getId());
    }

    @Override
    public PipelineConfBean getPipelineConfBean(String module, BranchType branchType) {
        PipelineConf pipelineConf = pipelineConfDao.get(module, branchType);
        if (pipelineConf == null) {
            return null;
        }
        Long pipelineConfId = pipelineConf.getId();
        return getPipelineConfBean(pipelineConfId);
    }

    private PipelineConfBean initDefaultPipelineConf(String module, String pipelineName,
                                                     BranchType branchType, String remark) {
        PipelineConfBean defaultPipelineConf = new PipelineConfBean();
        defaultPipelineConf.setAuto(true);
        defaultPipelineConf.setModule(module);
        defaultPipelineConf.setName(pipelineName);
        defaultPipelineConf.setBranchType(branchType);
        defaultPipelineConf.setRemark(remark);
        defaultPipelineConf.setAuto(true);

        List<JobConfBean> jobConfBeanList = new LinkedList<>();

        // 编译构建Job
        JobConfBean compileJobConf = new JobConfBean();
        compileJobConf.setPluginType(PluginType.COMPILE);
        compileJobConf.setName(DEFAULT_COMPILE_JOB_NAME);
        compileJobConf.setTriggerMode(TriggerMode.AUTO);
        compileJobConf.setPluginConf(new JSONObject());
        jobConfBeanList.add(compileJobConf);

        // 发版Job
        JobConfBean releaseJobConf = new JobConfBean();
        releaseJobConf.setPluginType(PluginType.RELEASE);
        releaseJobConf.setName(DEFAULT_RELEASE_JOB_NAME);
        releaseJobConf.setTriggerMode(TriggerMode.MANUAL);
        releaseJobConf.setPluginConf(new JSONObject());
        jobConfBeanList.add(releaseJobConf);
        defaultPipelineConf.setJobConfs(jobConfBeanList);
        return defaultPipelineConf;
    }

}
