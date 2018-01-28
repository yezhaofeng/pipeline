package com.jlu.plugin.instance.release.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.db.sqlcondition.DescOrder;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.plugin.instance.release.dao.IReleaseBuildDao;
import com.jlu.plugin.instance.release.model.ReleaseBuild;

/**
 * Created by langshiquan on 18/1/20.
 */
@Repository
public class ReleaseBuildDaoImpl extends AbstractBaseDao<ReleaseBuild> implements IReleaseBuildDao {

    @Override
    public List<ReleaseBuild> get(String owner, String module) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("owner", owner);
        conditionAndSet.put("module", module);
        conditionAndSet.put("status", PipelineJobStatus.SUCCESS);
        DescOrder descOrder = new DescOrder("id");
        List<ReleaseBuild> releaseBuilds = findByProperties(conditionAndSet, descOrder);
        return CollectionUtils.isEmpty(releaseBuilds) ? new ArrayList<>(0) : releaseBuilds;
    }

    @Override
    public ReleaseBuild getLastest(String owner, String module) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("owner", owner);
        conditionAndSet.put("module", module);
        conditionAndSet.put("status", PipelineJobStatus.SUCCESS);
        List<ReleaseBuild> releaseBuilds = findHeadByProperties(conditionAndSet, null, 0, 1);
        return CollectionUtils.isEmpty(releaseBuilds) ? null : releaseBuilds.get(0);
    }
}
