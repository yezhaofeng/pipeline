package com.jlu.plugin.dao.impl;

import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.utils.CollUtils;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.plugin.dao.IPluginInfoDao;
import com.jlu.plugin.model.PluginInfo;

import java.util.List;

/**
 * Created by yezhaofeng on 2019/1/15.
 */
@Repository
public class PluginInfoDaoImpl extends AbstractBaseDao<PluginInfo> implements IPluginInfoDao {

    @Override
    public PluginInfo findByJobType(String jobType) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("jobType", jobType);
        List<PluginInfo> pluginInfos = findByProperties(conditionAndSet);
        return CollUtils.isEmpty(pluginInfos) ? null : pluginInfos.get(0);
    }
}
