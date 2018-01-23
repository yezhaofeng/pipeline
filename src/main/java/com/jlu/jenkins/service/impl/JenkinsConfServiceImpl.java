package com.jlu.jenkins.service.impl;

import com.jlu.jenkins.bean.JenkinsJobsBean;
import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.offbytwo.jenkins.JenkinsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.jenkins.dao.IJenkinsConfDao;
import com.jlu.jenkins.model.JenkinsConf;
import com.jlu.jenkins.service.IJenkinsConfService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by langshiquan on 18/1/10.
 */
@Service
public class JenkinsConfServiceImpl implements IJenkinsConfService {

    @Autowired
    private IJenkinsConfDao jenkinsConfDao;

    @Autowired
    private IJenkinsServerService jenkinsServerService;

    @Override
    public void saveOrUpdate(JenkinsConf jenkinsConf) {
        jenkinsConfDao.saveOrUpdate(jenkinsConf);
    }

    @Override
    public JenkinsConf get(String serverUrl, String username, String password) {
        return jenkinsConfDao.find(serverUrl, username, password);
    }

    @Override
    public JenkinsConf get(Long id) {
        return jenkinsConfDao.findById(id);
    }

    @Override
    public List<JenkinsJobsBean> getByCreateUser(String createUser) throws IOException {
        List<JenkinsJobsBean> jenkinsJobs = new ArrayList<>();
        List<JenkinsConf> jenkinsConfs = jenkinsConfDao.find(createUser);
        for (JenkinsConf jenkinsConf : jenkinsConfs) {
            JenkinsJobsBean jenkinsJobsBean = new JenkinsJobsBean();
            jenkinsJobsBean.setServerUrl(jenkinsConf.getServerUrl());
            jenkinsJobsBean.setJenkinsServerId(jenkinsConf.getId());
            JenkinsServer jenkinsServer = jenkinsServerService.getJenkinsServer(jenkinsConf);
            Set<String> jobs = jenkinsServerService.getJobs(jenkinsServer);
            jenkinsJobsBean.setJobs(jobs);
            jenkinsJobs.add(jenkinsJobsBean);
        }
        return jenkinsJobs;
    }
}
