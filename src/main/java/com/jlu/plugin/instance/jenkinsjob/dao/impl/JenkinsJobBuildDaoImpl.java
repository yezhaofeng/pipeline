package com.jlu.plugin.instance.jenkinsjob.dao.impl;

import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.utils.CollUtils;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.plugin.instance.jenkinsjob.dao.IJenkinsJobBuildDao;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yezhaofeng on 2019/1/20.
 */
@Repository
public class JenkinsJobBuildDaoImpl extends AbstractBaseDao<JenkinsJobBuild> implements IJenkinsJobBuildDao {
    @Override
    public List<JenkinsJobBuild> get(Long jenkinsServerId, String jobName, Integer buildNumber) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("jenkinsServerId", jenkinsServerId);
        conditionAndSet.put("jobName", jobName);
        conditionAndSet.put("buildNumber", buildNumber);
        List<JenkinsJobBuild> jenkinsJobBuilds = findByProperties(conditionAndSet);
        return CollUtils.isEmpty(jenkinsJobBuilds) ? new LinkedList<>() : jenkinsJobBuilds;
    }
}
