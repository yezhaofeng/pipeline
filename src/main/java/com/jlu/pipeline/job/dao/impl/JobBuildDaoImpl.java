package com.jlu.pipeline.job.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.jlu.common.db.sqlcondition.DescOrder;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.plugin.bean.PluginType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.utils.CollUtils;
import com.jlu.pipeline.dao.IPipelineBuildDao;
import com.jlu.pipeline.job.dao.IJobBuildDao;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.model.PipelineBuild;

/**
 * Created by langshiquan on 18/1/15.
 */
@Repository
public class JobBuildDaoImpl extends AbstractBaseDao<JobBuild> implements IJobBuildDao {

    @Autowired
    private IPipelineBuildDao pipelineBuildDao;

    @Override
    public JobBuild getTopJob(Long pipelineBuildId) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("pipelineBuildId", pipelineBuildId);
        conditionAndSet.put("upStreamJobBuildId", 0L);
        List<JobBuild> jobBuilds = findByProperties(conditionAndSet);
        return CollUtils.isEmpty(jobBuilds) ? null : jobBuilds.get(0);
    }

    @Override
    public JobBuild getByUpStreamJobBuildId(Long upStreamJobBuildId) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("upStreamJobBuildId", upStreamJobBuildId);
        List<JobBuild> jobBuilds = findByProperties(conditionAndSet);
        return CollUtils.isEmpty(jobBuilds) ? null : jobBuilds.get(0);
    }

    @Override
    public List<JobBuild> getByPipelineBuildId(Long pipelineBuildId) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("pipelineBuildId", pipelineBuildId);
        List<JobBuild> jobBuilds = findByProperties(conditionAndSet);
        return CollUtils.isEmpty(jobBuilds) ? new ArrayList<>(0) : jobBuilds;
    }

    @Override
    public String getModuleById(Long id) {
        JobBuild jobBuild = findById(id);
        if (jobBuild == null) {
            return StringUtils.EMPTY;
        }
        Long pipelineBuildId = jobBuild.getPluginBuildId();
        PipelineBuild pipelineBuild = pipelineBuildDao.findById(pipelineBuildId);
        if (pipelineBuild == null) {
            return StringUtils.EMPTY;
        }
        return pipelineBuild.getModule();
    }

    @Override
    public JobBuild getLastBuild(Long pipelineBuildId, PluginType pluginType, PipelineJobStatus jobStatus) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("pipelineBuildId", pipelineBuildId);
        conditionAndSet.put("pluginType", pluginType);
        conditionAndSet.put("jobStatus", jobStatus);
        DescOrder descOrder = new DescOrder("id");
        List<JobBuild> jobBuilds = findByProperties(conditionAndSet, descOrder);
        return CollUtils.isEmpty(jobBuilds) ? null : jobBuilds.get(0);
    }
}
