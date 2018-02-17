package com.jlu.pipeline.dao.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.db.sqlcondition.DescOrder;
import com.jlu.common.db.sqlcondition.OrderCondition;
import com.jlu.common.utils.CollUtils;
import com.jlu.pipeline.dao.IPipelineBuildDao;
import com.jlu.pipeline.model.PipelineBuild;

/**
 * Created by langshiquan on 18/1/15.
 */
@Repository
public class PipelineBuildDaoImpl extends AbstractBaseDao<PipelineBuild> implements IPipelineBuildDao {

    @Override
    public Long getNextBuildNumber(String module) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("module", module);
        List<OrderCondition> orders = new ArrayList<OrderCondition>();
        orders.add(new DescOrder("buildNumber"));
        List<PipelineBuild> pipelineBuilds = findHeadByProperties(conditionAndSet, orders, 0, 1);
        return CollUtils.isEmpty(pipelineBuilds) ? 1L : pipelineBuilds.get(0).getBuildNumber() + 1L;
    }

    @Override
    public List<PipelineBuild> get(Long pipelineConfId) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("pipelineConfId", pipelineConfId);
        DescOrder descOrder = new DescOrder("id");
        List<PipelineBuild> pipelineBuilds = findByProperties(conditionAndSet,descOrder);
        return CollUtils.isEmpty(pipelineBuilds) ? new LinkedList<>() : pipelineBuilds;
    }

    @Override
    public List<PipelineBuild> get(Long pipelineConfId, String branchName) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("pipelineConfId", pipelineConfId);
        conditionAndSet.put("branch",branchName);
        DescOrder descOrder = new DescOrder("id");
        List<PipelineBuild> pipelineBuilds = findByProperties(conditionAndSet,descOrder);
        return CollUtils.isEmpty(pipelineBuilds) ? new LinkedList<>() : pipelineBuilds;
    }
}
