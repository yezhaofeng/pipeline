package com.jlu.jenkins.dao;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.jenkins.model.JenkinsConf;

import java.util.List;

/**
 * Created by langshiquan on 18/1/10.
 */

public interface IJenkinsConfDao extends IBaseDao<JenkinsConf> {
    JenkinsConf find(String serverUrl, String username, String password);

    List<JenkinsConf> find(String createUser);
}