package com.jlu.jenkins.dao.impl;

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
    public JenkinsConf findByServerUrlAndUserNameAndPassword(String serverUrl, String username, String password) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("serverUrl", serverUrl);
        conditionAndSet.put("masterUser", username);
        conditionAndSet.put("masterPassword", password);

        List<JenkinsConf> jenkinsConfList = findByProperties(conditionAndSet);
        return CollectionUtils.isEmpty(jenkinsConfList) ? null : jenkinsConfList.get(0);
    }
}
