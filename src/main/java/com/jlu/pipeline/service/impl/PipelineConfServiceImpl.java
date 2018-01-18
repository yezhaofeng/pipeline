package com.jlu.pipeline.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlu.pipeline.bean.PipelineConfBean;
import com.jlu.pipeline.dao.PipelineConfDao;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.service.JobConfService;
import com.jlu.pipeline.model.PipelineConf;
import com.jlu.pipeline.service.PipelineConfService;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class PipelineConfServiceImpl implements PipelineConfService {

    @Autowired
    private PipelineConfDao pipelineConfDao;

    @Autowired
    private JobConfService jobConfService;

    @Transactional
    @Override
    public void processPipeline(PipelineConfBean pipelineConfBean, String userName) {
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
            pipelineConf.setCreateUser(userName);
        }

        pipelineConf.setLastModifiedUser(userName);
        pipelineConf.setLastModifiedTime(new Date());
        pipelineConfDao.save(pipelineConf);
        List<JobConfBean> jobConfBeans = pipelineConfBean.getJobConfs();
        jobConfService.processJob(jobConfBeans, pipelineConf.getId());
    }

    @Override
    public PipelineConfBean getPipelineConfBean(Long pipelineConfId) {
        PipelineConfBean pipelineConfBean = new PipelineConfBean();
        if (pipelineConfId == null && pipelineConfId == 0L) {
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
    public PipelineConf getPipelineConfBean(String name, String module, String branchName) {
        return null;
    }
}
