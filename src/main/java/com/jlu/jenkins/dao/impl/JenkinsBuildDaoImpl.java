package com.jlu.jenkins.dao.impl;


import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.utils.CollUtils;
import com.jlu.jenkins.dao.IJenkinsBuildDao;
import com.jlu.jenkins.model.JenkinsBuild;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */
@Repository
public class JenkinsBuildDaoImpl extends AbstractBaseDao<JenkinsBuild> implements IJenkinsBuildDao {

    @Override
    public List<JenkinsBuild> findWithoutBuildResult() {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("buildResult", null);
        List<JenkinsBuild> jenkinsBuilds = findByProperties(conditionAndSet);
        return jenkinsBuilds == null ? new ArrayList<>(0) : jenkinsBuilds;
    }
}
