package com.jlu.pipeline.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
 * Created by yezhaofeng on 2019/1/14.
 */
@Service
public class PipelineConfServiceImpl implements IPipelineConfService {

    @Autowired
    private IPipelineConfDao pipelineConfDao;

    @Autowired
    private IJobConfService jobConfService;

    @Override
    public void processPipelineWithTransaction(PipelineConfBean pipelineConfBean) {
        checkValid(pipelineConfBean);
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


    private void checkValid(PipelineConfBean pipelineConfBean) {
        List<Integer> compileJobIndexs = new ArrayList<>();
        List<Integer> releaseJobIndexs = new ArrayList<>();
        List<JobConfBean> jobConfBeanList = pipelineConfBean.getJobConfs();
        for (int i = 0; i < jobConfBeanList.size(); i++) {
            JobConfBean jobConfBean = jobConfBeanList.get(i);
            // 名字不能为空
            if (StringUtils.isBlank(jobConfBean.getName())) {
                // i + 1  自然序列从1开始
                throw new PipelineRuntimeException(String.format("第%d个Job的名字不能为空", i + 1));
            }
            if (PluginType.COMPILE.equals(jobConfBean.getPluginType())) {
                compileJobIndexs.add(i);
            }
            if (PluginType.RELEASE.equals(jobConfBean.getPluginType())) {
                releaseJobIndexs.add(i);
            }
        }
        // 最多只能配置一个编译Job
        if (compileJobIndexs.size() > 1) {
            throw new PipelineRuntimeException("仅允许配置一个'编译构建'Job");
        }
        // 最多只能配置一个发版Job
        if (releaseJobIndexs.size() > 1) {
            throw new PipelineRuntimeException("仅允许配置一个'发版'Job");
        }
        // 不能只配置一个发版Job，而不配置编译Job
        if (compileJobIndexs.size() == 0 && releaseJobIndexs.size() == 1) {
            throw new PipelineRuntimeException("'发版'Job依赖'编译构建'Job，请在此之前配置'编译构建'Job");
        }
        // 若配置发版Job，那么之前必须有编译Job
        if (compileJobIndexs.size() == 1 && releaseJobIndexs.size() == 1) {
            if (compileJobIndexs.get(0) > releaseJobIndexs.get(0)) {
                throw new PipelineRuntimeException("'编译构建'Job必须配置在'发版'Job之前");
            }
        }
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

    private static final String DEFAULT_MASTER_PIPELINE_NAME = "MasterPipeline";
    private static final String DEFAULT_BRANCH_PIPELINE_NAME = "BranchPipeline";
    private static final String DEFAULT_MASTER_PIPELINE_REMARK = "主干";
    private static final String DEFAULT_BRANCH_PIPELINE_REMARK = "分支";
    private static final String DEFAULT_COMPILE_JOB_NAME = "构建";
    private static final String DEFAULT_RELEASE_JOB_NAME = "发布";

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
