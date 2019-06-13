package com.jlu.pipeline.job.service.impl;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jlu.common.utils.CollUtils;
import com.jlu.common.utils.JsonUtils;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.dao.IJobConfDao;
import com.jlu.pipeline.job.model.JobConf;
import com.jlu.pipeline.job.service.IJobConfService;
import com.jlu.plugin.AbstractPlugin;
import com.jlu.plugin.service.IPluginInfoService;

/**
 * Created by yezhaofeng on 2019/1/14.
 */
@Service
public class JobConfServiceImpl implements IJobConfService {

    private final Long topJobId = 0L;

    @Autowired
    private IJobConfDao jobConfDao;

    @Autowired
    private IPluginInfoService pluginInfoService;

    @Override
    public JobConf processJobWithTransaction(JobConfBean jobConfBean, Long pipelineConfId) {
        JobConf jobConf = null;
        Long jobConfId = jobConfBean.getId();
        if (jobConfId != null) {
            jobConf = jobConfDao.findById(jobConfId);
        }
        if (jobConf == null) {
            jobConf = new JobConf();
            BeanUtils.copyProperties(jobConfBean, jobConf);
        } else {
            jobConf = jobConfDao.findById(jobConfId);
            BeanUtils.copyProperties(jobConfBean, jobConf);
            jobConf.setId(jobConfId);
        }

        Map<String, String> parameterMap = jobConfBean.getParameterMap();
        String params = JSON.toJSONString(parameterMap);
        if (StringUtils.isBlank(params)) {
            params = JobConf.DEFAULT_PARAMS;
        }
        jobConf.setParams(params);
        jobConf.setPipelineConfId(pipelineConfId);
        AbstractPlugin abstractPlugin = pluginInfoService.getRealJobPlugin(jobConf.getPluginType());
        Long pluginConfId = abstractPlugin.getDataOperator().saveConf(jobConfBean.getPluginConf());
        jobConf.setPluginConfId(pluginConfId);
        jobConfDao.saveOrUpdate(jobConf);
        return jobConf;
    }

    @Override
    public void processJobWithTransaction(List<JobConfBean> jobConfBeans, Long pipelineConfId) {
        List<JobConf> jobConfsInDb = jobConfDao.get(pipelineConfId, false);
        if (CollUtils.isEmpty(jobConfBeans)) {
            return;
        }
        Long upStreamJobConfId = topJobId;
        for (int i = 0; jobConfBeans != null && i < jobConfBeans.size(); i++) {
            JobConfBean confBean = jobConfBeans.get(i);
            confBean.setUpStreamJobConfId(upStreamJobConfId);
            JobConf jobConf = processJobWithTransaction(confBean, pipelineConfId);
            // remove normal job
            if (jobConfsInDb != null) {
                Iterator<JobConf> jobConfIterator = jobConfsInDb.iterator();
                while (jobConfIterator.hasNext()) {
                    JobConf currentJobConf = jobConfIterator.next();
                    Long currentId = currentJobConf.getId();
                    Long id = jobConf.getId();
                    if (currentId.equals(id)) {
                        jobConfIterator.remove();
                    }
                }
            }

            upStreamJobConfId = jobConf.getId();
        }

        deleteJobConf(jobConfsInDb);
    }

    @Override
    public List<JobConfBean> getJobConfs(Long pipelineConfId) {
        List<JobConfBean> jobConfBeanList = new ArrayList<>();
        List<JobConf> jobConfs = jobConfDao.get(pipelineConfId, false);

        for (JobConf jobConf : jobConfs) {
            JobConfBean jobConfBean = new JobConfBean();
            BeanUtils.copyProperties(jobConf, jobConfBean);
            String params = jobConf.getParams();
            Map<String, String> parameterMap = (Map<String, String>) JSON.parse(params);
            if (parameterMap == null) {
                parameterMap = new HashMap<>(0);
            }
            jobConfBean.setParameterMap(parameterMap);
            jobConfBean.setPluginConf((JSONObject) JsonUtils
                    .getJsonObject(pluginInfoService.getRealJobPlugin(jobConf.getPluginType()).getDataOperator()
                            .getConf(jobConf.getPluginConfId())));
            jobConfBeanList.add(jobConfBean);
        }
        return sortJobConfBeanList(jobConfBeanList);
    }

    // 按照job上下游关系排序 (上游job的id = 下游job的upStreamJobConfId)
    private List<JobConfBean> sortJobConfBeanList(List<JobConfBean> jobConfBeanList) {
        List<JobConfBean> sortedJobConfBeanList = new ArrayList<>();
        int size = jobConfBeanList.size();

        Long upStreamJobConfId = 0L;
        int index = 0;
        while (index < size) {
            JobConfBean jobConfBean = getJobConfBeanByUpStreamJobConfId(jobConfBeanList, upStreamJobConfId);
            sortedJobConfBeanList.add(jobConfBean);
            upStreamJobConfId = jobConfBean.getId();
            index++;
        }
        return sortedJobConfBeanList;
    }

    private JobConfBean getJobConfBeanByUpStreamJobConfId(List<JobConfBean> jobConfBeanList, Long upStreamJobConfId) {
        for (JobConfBean jobConfBean : jobConfBeanList) {
            if (jobConfBean.getUpStreamJobConfId().equals(upStreamJobConfId)) {
                return jobConfBean;
            }
        }
        return null;
    }

    private void deleteJobConf(List<JobConf> jobConfs) {
        if (CollUtils.isEmpty(jobConfs)) {
            return;
        }
        for (JobConf jobConf : jobConfs) {
            jobConf.setDeleteStatus(true);
            jobConfDao.saveOrUpdate(jobConf);
        }
    }
}
