package com.jlu.jenkins.service;

import java.io.IOException;
import java.util.List;

import com.jlu.jenkins.bean.JenkinsJobsBean;
import com.jlu.jenkins.model.JenkinsConf;

/**
 * Created by yezhaofeng on 2019/1/10.
 */
public interface IJenkinsConfService {

    JenkinsConf get(String serverUrl, String username, String password);

    JenkinsConf get(Long id);

    List<JenkinsConf> getConfByCreateUser(String createUser) throws IOException;

    List<JenkinsJobsBean> getJobsByCreateUser(String createUser) throws IOException;

    void saveOrUpdate(JenkinsConf jenkinsConf);

    void save(JenkinsConf jenkinsConf);

    void delete(Long jenkinsServerId);

}
