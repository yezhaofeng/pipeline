package com.jlu.jenkins.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.jenkins.dao.JenkinsConfDao;
import com.jlu.jenkins.model.JenkinsConf;
import com.jlu.jenkins.service.IJenkinsConfService;

/**
 * Created by langshiquan on 18/1/10.
 */
@Service
public class JenkinsConfServiceImpl implements IJenkinsConfService {

    @Autowired
    private JenkinsConfDao jenkinsConfDao;

    @Override
    public void saveOrUpdate(JenkinsConf jenkinsConf) {
        jenkinsConfDao.save(jenkinsConf);
    }
}
