package com.jlu.jenkins.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.jenkins.dao.IJenkinsConfDao;
import com.jlu.jenkins.model.JenkinsConf;

/**
 * Created by langshiquan on 18/1/15.
 */
@Repository
public class JenkinsConfDaoImpl extends AbstractBaseDao<JenkinsConf> implements IJenkinsConfDao {
    @Override
    public JenkinsConf find(String serverUrl, String username, String password) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("serverUrl", serverUrl);
        conditionAndSet.put("masterUser", username);
        conditionAndSet.put("masterPassword", password);
        conditionAndSet.put("deleteStatus", false);
        List<JenkinsConf> jenkinsConfList = findByProperties(conditionAndSet);
        return CollectionUtils.isEmpty(jenkinsConfList) ? null : jenkinsConfList.get(0);
    }

    @Override
    public List<JenkinsConf> find(String createUser) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("createUser", createUser);
        conditionAndSet.put("deleteStatus", false);
        List<JenkinsConf> jenkinsConfList = findByProperties(conditionAndSet);
        return CollectionUtils.isEmpty(jenkinsConfList) ? new ArrayList<>(0) : jenkinsConfList;
    }
}
