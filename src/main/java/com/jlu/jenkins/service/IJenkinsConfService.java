package com.jlu.jenkins.service;

import com.jlu.jenkins.bean.JenkinsJobsBean;
import com.jlu.jenkins.model.JenkinsConf;

import java.io.IOException;
import java.util.List;

/**
 * Created by langshiquan on 18/1/10.
 */
public interface IJenkinsConfService {
    void saveOrUpdate(JenkinsConf jenkinsConf);

    JenkinsConf get(String serverUrl, String username, String password);

    JenkinsConf get(Long id);

    List<JenkinsJobsBean> getByCreateUser(String createUser) throws IOException;
}
