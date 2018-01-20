package com.jlu.plugin.instance.jenkinsjob.dao.impl;

import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.plugin.instance.jenkinsjob.dao.IJenkinsJobBuildDao;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;

/**
 * Created by langshiquan on 18/1/20.
 */
@Repository
public class JenkinsJobBuildDaoImpl extends AbstractBaseDao<JenkinsJobBuild> implements IJenkinsJobBuildDao {
}
