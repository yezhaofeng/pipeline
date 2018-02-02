package com.jlu.jenkins.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.common.utils.CollUtils;
import com.jlu.jenkins.bean.JenkinsJobsBean;
import com.jlu.jenkins.dao.IJenkinsConfDao;
import com.jlu.jenkins.model.JenkinsConf;
import com.jlu.jenkins.service.IJenkinsConfService;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.offbytwo.jenkins.JenkinsServer;

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
    public List<JenkinsConf> getConfByCreateUser(String createUser) throws IOException {
        return jenkinsConfDao.find(createUser);
    }

    @Override
    public List<JenkinsJobsBean> getJobsByCreateUser(String createUser) throws IOException {
        List<JenkinsJobsBean> jenkinsJobs = new ArrayList<>();

        // 防止相同的jenkins配置重复出现，重写了JenkinsConf的hashcode和equals
        Set<JenkinsConf> jenkinsConfs = new HashSet<>();
        List<JenkinsConf> jenkinsConfList = jenkinsConfDao.find(createUser);
        if (CollUtils.isEmpty(jenkinsConfList)) {
            return jenkinsJobs;
        }

        CollUtils.addAll(jenkinsConfs, jenkinsConfList.iterator());
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

    @Override
    public void save(JenkinsConf jenkinsConf) {
        jenkinsConfDao.save(jenkinsConf);
    }

    @Override
    public void delete(Long jenkinsServerId) {
        JenkinsConf jenkinsConf = jenkinsConfDao.findById(jenkinsServerId);
        if (jenkinsConf == null) {
            return;
        }
        jenkinsConf.setDeleteStatus(true);
        jenkinsConfDao.update(jenkinsConf);
    }
}
