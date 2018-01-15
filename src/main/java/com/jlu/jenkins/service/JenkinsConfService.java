package com.jlu.jenkins.service;

import com.jlu.jenkins.model.JenkinsConf;

/**
 * Created by langshiquan on 18/1/10.
 */
public interface JenkinsConfService {
    void saveOrUpdate(JenkinsConf jenkinsConf);
}
