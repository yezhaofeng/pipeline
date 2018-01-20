package com.jlu.jenkins.dao.impl;

import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.jenkins.dao.IJenkinsConfDao;
import com.jlu.jenkins.model.JenkinsConf;

/**
 * Created by langshiquan on 18/1/15.
 */
@Repository
public class JenkinsConfDaoImpl extends AbstractBaseDao<JenkinsConf> implements IJenkinsConfDao {
}
