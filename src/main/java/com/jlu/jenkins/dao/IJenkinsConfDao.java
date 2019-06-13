package com.jlu.jenkins.dao;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.jenkins.model.JenkinsConf;

import java.util.List;

/**
 * Created by yezhaofeng on 2019/1/10.
 */

public interface IJenkinsConfDao extends IBaseDao<JenkinsConf> {
    JenkinsConf find(String serverUrl, String username, String password);

    List<JenkinsConf> find(String createUser);

    String findCreateUserById(Long id);
}